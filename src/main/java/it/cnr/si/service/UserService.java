package it.cnr.si.service;

import feign.FeignException;
import it.cnr.si.config.Constants;
import it.cnr.si.domain.Authority;
import it.cnr.si.domain.User;
import it.cnr.si.repository.AuthorityRepository;
import it.cnr.si.repository.UserRepository;
import it.cnr.si.security.ACEAuthentication;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.dto.UserDTO;

import it.cnr.si.service.dto.anagrafica.enums.TipoAppartenenza;
import it.cnr.si.service.dto.anagrafica.enums.TipoRuolo;
import it.cnr.si.service.dto.anagrafica.scritture.BossDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleRuoloWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleUtenteWebDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
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

    private final AceService aceService;

    @Value("#{'${ace.contesto}'.split(',')}")
    private List<String> contestoACE;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, AceService aceService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.aceService = aceService;
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
        Map<String, Object> details = (Map<String, Object>) authentication.getUserAuthentication().getDetails();

        // convert Authorities to GrantedAuthorities
        Set<GrantedAuthority> grantedAuthorities = getRoles(details).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        User user = getUser(details);
        user.setAuthorities(
            grantedAuthorities
                .stream()
                .map(a -> {
                    Authority auth = new Authority();
                    auth.setName(a.getAuthority());
                    return auth;
                })
                .collect(Collectors.toSet())
        );

        //---
        String principal = ((String) details.get("username_cnr")).toLowerCase();
        List<Authority> authorities = new ArrayList<>();
        List<BossDto> bossDtos = new ArrayList<>();
        List<SimpleRuoloWebDto> srwDtos = new ArrayList<>();
        Boolean principalIsInAce = true;
        try {
            bossDtos = aceService.ruoliUtenteAttivi(principal);
            srwDtos = aceService.ruoliAttivi(principal);
        } catch (Exception e) {
            principalIsInAce = false;
            System.out.println (e.getMessage());
        }
        authorities.addAll(
            bossDtos.stream()
                .filter(bossDto -> contestoACE.contains(bossDto.getRuolo().getContesto().getSigla()))
                .filter(bossDto -> {
                    return !(bossDto.getEntitaOrganizzativa() != null && bossDto.getRuolo().getTipoRuolo().equals(TipoRuolo.ROLE_ADMIN));
                })
                .map(a -> {
                    Authority auth = new Authority();
                    auth.setName(Optional.ofNullable(a.getRuolo().getTipoRuolo()).map(TipoRuolo::name).orElse(AuthoritiesConstants.USER));
                    return auth;
                })
                .distinct()
                .collect(Collectors.toList()));

        Stream<SimpleEntitaOrganizzativaWebDto> entitaOrganizzativaAssegnata = bossDtos.stream()
            .filter(bossDto -> contestoACE.contains(bossDto.getRuolo().getContesto().getSigla()))
            .filter(bossDto -> Optional.ofNullable(bossDto.getEntitaOrganizzativa()).isPresent())
            .map(bossDto -> bossDto.getEntitaOrganizzativa());

        if (bossDtos.isEmpty()) {
            authorities.addAll(
                srwDtos.stream()
                    .filter(ruoloWebDto -> contestoACE.contains(ruoloWebDto.getContesto().getSigla()))
                    .map(a -> {
                        Authority auth = new Authority();
                        auth.setName(Optional.ofNullable(a.getTipoRuolo()).map(TipoRuolo::name).orElse(AuthoritiesConstants.USER));
                        return auth;
                    })
                    .distinct()
                    .collect(Collectors.toList()));
        }
        if (authorities.isEmpty()) {
            //
        }

        if(principalIsInAce) {
            Authority authUser = new Authority();
            authUser.setName(AuthoritiesConstants.USER);
            authorities.add(authUser);
        }
        user.getAuthorities().addAll(authorities);

        try {
            SimpleUtenteWebDto utenteWebDto = aceService.getUtente(principal);
            if (Optional.ofNullable(utenteWebDto.getPersona()).isPresent()) {
                List<SimpleEntitaOrganizzativaWebDto> entitaOrganizzativeStruttura =
                    aceService.findEntitaOrganizzativeStruttura(principal, LocalDate.now(), TipoAppartenenza.SEDE);
                //return new ACEAuthentication(utente, utenteWebDto, authentication, authorities,
                //    Stream.concat(
                //        entitaOrganizzativaAssegnata.map(SimpleEntitaOrganizzativaWebDto::getCdsuo).distinct(),
                //        entitaOrganizzativeStruttura.stream().map(SimpleEntitaOrganizzativaWebDto::getCdsuo).distinct()
                //    ).collect(Collectors.toList())
                //);
            }
        } catch (FeignException e) {
            log.warn("Person not found for principal {}", principal);
        }


        //---

        Set<Authority> userAuthorities = new HashSet<>();
        for(GrantedAuthority authority: grantedAuthorities){
            Authority auth = new Authority();
            auth.setName(authority.getAuthority());
            userAuthorities.add(auth);
        }
        user.getAuthorities().addAll(userAuthorities);

        UsernamePasswordAuthenticationToken token = getToken(details, user, grantedAuthorities);
        Object oauth2AuthenticationDetails = authentication.getDetails(); // should be an OAuth2AuthenticationDetails
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

    private  List<String> getRoles(Map<String, Object> details) {

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
        user.setId(1L);
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

}
