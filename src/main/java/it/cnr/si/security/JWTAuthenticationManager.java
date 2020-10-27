package it.cnr.si.security;

import feign.FeignException;
import it.cnr.si.service.AceService;
import it.cnr.si.service.AuthService;
import it.cnr.si.service.dto.anagrafica.AceJwt;
import it.cnr.si.service.dto.anagrafica.enums.TipoRuolo;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.RuoloWebDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationManager implements AuthenticationManager {

    public static final String DIRETTORE = "direttore";
    private final Logger log = LoggerFactory.getLogger(JWTAuthenticationManager.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AceService aceService;

    @Value("${ace.contesto}")
    private String contestoACE;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();
        // login ACE
        try {
            authService.getToken(principal, credentials);
        } catch (Exception _ex) {
            log.error("Authentication failed for user {}", principal);
            throw new BadCredentialsException("login.messages.error.authentication");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<RuoloWebDto> ruoloWebDtos = aceService.ruoliAttivi(principal);
        authorities.addAll(
            ruoloWebDtos.stream()
                .filter(ruolo -> ruolo.getContesto().getSigla().equals(contestoACE))
                .map(a -> new SimpleGrantedAuthority(Optional.ofNullable(a.getTipoRuolo()).map(TipoRuolo::name).orElse(AuthoritiesConstants.USER)))
                .collect(Collectors.toList()));
        authorities.addAll(
            ruoloWebDtos.stream()
                .filter(ruolo -> ruolo.getSigla().equals(DIRETTORE))
                .map(a -> new SimpleGrantedAuthority(AuthoritiesConstants.SUPERUSER))
                .collect(Collectors.toList()));

        if (authorities.isEmpty())
            throw new InsufficientAuthenticationException("login.messages.error.insufficient-authentication");
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        User utente = new User(principal, credentials, authorities);
        try {
            final PersonaWebDto personaByUsername = aceService.getPersonaByUsername(principal);
            return new ACEAuthentication(utente, authentication, authorities,
                Optional.ofNullable(personaByUsername)
                    .flatMap(personaWebDto -> Optional.ofNullable(personaWebDto.getSede()))
                    .orElse(null)
            );
        } catch (FeignException e) {
            log.warn("Person not found for principal {}", principal);
        }
        return new UsernamePasswordAuthenticationToken(utente, authentication, authorities);
    }

}
