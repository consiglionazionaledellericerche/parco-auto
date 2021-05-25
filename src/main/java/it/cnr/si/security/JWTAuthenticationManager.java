/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.security;

import feign.FeignException;
import it.cnr.si.service.AceService;
import it.cnr.si.service.AuthService;
import it.cnr.si.service.dto.anagrafica.enums.TipoAppartenenza;
import it.cnr.si.service.dto.anagrafica.enums.TipoRuolo;
import it.cnr.si.service.dto.anagrafica.scritture.BossDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimplePersonaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleRuoloWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleUtenteWebDto;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String principal = Optional.ofNullable(authentication.getPrincipal())
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .map(String::toLowerCase)
            .orElseThrow(() -> new BadCredentialsException("login.messages.error.authentication"));
        String credentials = (String) authentication.getCredentials();
        // login ACE
        try {
            authService.getToken(principal, credentials);
        } catch (Exception _ex) {
            log.error("Authentication failed for user {}", principal);
            throw new BadCredentialsException("login.messages.error.authentication");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<BossDto> bossDtos = aceService.ruoliUtenteAttivi(principal);
        authorities.addAll(
            bossDtos.stream()
                .filter(bossDto -> bossDto.getRuolo().getContesto().getSigla().equals(contestoACE))
                .filter(bossDto -> {
                    return !(bossDto.getEntitaOrganizzativa() != null && bossDto.getRuolo().getTipoRuolo().equals(TipoRuolo.ROLE_ADMIN));
                })
                .map(a -> new SimpleGrantedAuthority(
                    Optional.ofNullable(a.getRuolo().getTipoRuolo()).map(TipoRuolo::name).orElse(AuthoritiesConstants.USER))
                )
                .distinct()
                .collect(Collectors.toList()));

        Stream<SimpleEntitaOrganizzativaWebDto> entitaOrganizzativaAssegnata = bossDtos.stream()
            .filter(bossDto -> bossDto.getRuolo().getContesto().getSigla().equals(contestoACE))
            .filter(bossDto -> Optional.ofNullable(bossDto.getEntitaOrganizzativa()).isPresent())
            .map(bossDto -> bossDto.getEntitaOrganizzativa());

        if (bossDtos.isEmpty()) {
            authorities.addAll(
                aceService.ruoliAttivi(principal).stream()
                    .filter(ruoloWebDto -> ruoloWebDto.getContesto().getSigla().equals(contestoACE))
                    .map(a -> new SimpleGrantedAuthority(
                        Optional.ofNullable(a.getTipoRuolo()).map(TipoRuolo::name).orElse(AuthoritiesConstants.USER))
                    )
                    .distinct()
                    .collect(Collectors.toList()));
        }
        if (authorities.isEmpty())
            throw new InsufficientAuthenticationException("login.messages.error.insufficient-authentication");
        if (!authorities.contains(new SimpleGrantedAuthority(AuthoritiesConstants.USER))) {
            authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        }
        User utente = new User(principal, credentials, authorities);
        try {
            SimpleUtenteWebDto utenteWebDto = aceService.getUtente(principal);
            if (Optional.ofNullable(utenteWebDto.getPersona()).isPresent()) {
                List<SimpleEntitaOrganizzativaWebDto> entitaOrganizzativeStruttura =
                    aceService.findEntitaOrganizzativeStruttura(principal, LocalDate.now(), TipoAppartenenza.SEDE);
                return new ACEAuthentication(utente, utenteWebDto, authentication, authorities,
                    Stream.concat(
                        entitaOrganizzativaAssegnata.map(SimpleEntitaOrganizzativaWebDto::getCdsuo).distinct(),
                        entitaOrganizzativeStruttura.stream().map(SimpleEntitaOrganizzativaWebDto::getCdsuo).distinct()
                    ).collect(Collectors.toList())
                );
            }
        } catch (FeignException e) {
            log.warn("Person not found for principal {}", principal);
        }
        return new UsernamePasswordAuthenticationToken(utente, authentication, authorities);
    }

}
