package it.cnr.si.service;

import it.cnr.si.config.Constants;
import it.cnr.si.domain.Authority;
import it.cnr.si.domain.User;
import it.cnr.si.repository.AuthorityRepository;
import it.cnr.si.repository.UserRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.dto.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    /**
     * Returns the user for a OAuth2 authentication.
     * Synchronizes the user in the local repository
     *
     * @param authentication OAuth2 authentication
     * @return the user from the authentication
     */
    @SuppressWarnings("unchecked")
    public UserDTO getUserFromAuthentication(OAuth2Authentication authentication) {
        Object oauth2AuthenticationDetails = authentication.getDetails(); // should be an OAuth2AuthenticationDetails
        Map<String, Object> details = (Map<String, Object>) authentication.getUserAuthentication().getDetails();
        User user = getUser(details);
        Set<Authority> userAuthorities = extractAuthorities(authentication, details);
        Authority roleUser = new Authority();
        roleUser.setName(AuthoritiesConstants.USER);
        userAuthorities.add(roleUser);
        user.setAuthorities(userAuthorities);

        // convert Authorities to GrantedAuthorities
        Set<GrantedAuthority> grantedAuthorities = getRoles(details).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

        UsernamePasswordAuthenticationToken token = getToken(details, user, grantedAuthorities);
        authentication = new OAuth2Authentication(authentication.getOAuth2Request(), token);
        authentication.setDetails(oauth2AuthenticationDetails); // must be present in a gateway for TokenRelayFilter to work
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new UserDTO(user);
    }


    private static UsernamePasswordAuthenticationToken getToken(Map<String, Object> details, User user, Set<GrantedAuthority> grantedAuthorities) {
        // create UserDetails so #{principal.username} works
        UserDetails userDetails =
            new org.springframework.security.core.userdetails.User(user.getLogin(),
                "N/A", grantedAuthorities);
        // update Spring Security Authorities to match groups claim from IdP
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            userDetails, "N/A", grantedAuthorities);
        token.setDetails(details);
        return token;
    }

    @SuppressWarnings("unchecked")
    private static Set<Authority> extractAuthorities(OAuth2Authentication authentication, Map<String, Object> details) {
        Set<Authority> userAuthorities;
        // get roles from details
        if (details.get("roles") != null) {
            userAuthorities = extractAuthorities((List<String>) details.get("roles"));
            // if roles don't exist, try groups
        } else if (details.get("groups") != null) {
            userAuthorities = extractAuthorities((List<String>) details.get("groups"));
        } else {
            userAuthorities = authoritiesFromStringStream(
                authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
            );
        }
        // convert Authorities to GrantedAuthorities
        userAuthorities.addAll(authoritiesFromStringStream(getRoles(details).stream()));

        return userAuthorities;
    }

    private static List<String> getRoles(Map<String, Object> details) {

        List list = new ArrayList();
        try {
            Map context = (Map) ((Map) details.get("contexts")).get("parcoauto-app");
            if(context != null) {
                list = (List) context.get("roles");
            }
        } catch (Exception e) {
            // TODO: inserire log....
            e.printStackTrace();
        }

        return list;
    }

    private static User getUser(Map<String, Object> details) {
        User user = new User();
        user.setId((String) details.get("sub"));
        user.setLogin(((String) details.get("username_cnr")).toLowerCase());
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        }
        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }
        if (details.get("email_verified") != null) {
            user.setActivated((Boolean) details.get("email_verified"));
        }
        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        }
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            String locale = (String) details.get("locale");
            if (locale.contains("-")) {
                String langKey = locale.substring(0, locale.indexOf("-"));
                user.setLangKey(langKey);
            } else if (locale.contains("_")) {
                String langKey = locale.substring(0, locale.indexOf("_"));
                user.setLangKey(langKey);
            }
        }
        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }
        user.setActivated(true);
        return user;
    }

    private static Set<Authority> extractAuthorities(List<String> values) {
        return authoritiesFromStringStream(
            values.stream().filter(role -> role.startsWith("ROLE_"))
        );
    }

    private static Set<Authority> authoritiesFromStringStream(Stream<String> strings) {
        return strings
            .map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
    }
}
