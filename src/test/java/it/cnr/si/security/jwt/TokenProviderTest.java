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

package it.cnr.si.security.jwt;

import it.cnr.si.security.AuthoritiesConstants;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Key;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenProviderTest {

    private final Base64.Encoder encoder = Base64.getEncoder();
    private final long ONE_MINUTE = 60000;
    private Key key;
    private JHipsterProperties jHipsterProperties;
    private TokenProvider tokenProvider;

    @Before
    public void setup() {
        jHipsterProperties = Mockito.mock(JHipsterProperties.class);
        tokenProvider = new TokenProvider(jHipsterProperties);
        key = Keys.hmacShaKeyFor(Decoders.BASE64
            .decode("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8"));

        ReflectionTestUtils.setField(tokenProvider, "key", key);
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", ONE_MINUTE);
    }

    @Test
    public void testReturnFalseWhenJWThasInvalidSignature() {
        boolean isTokenValid = tokenProvider.validateToken(createTokenWithDifferentSignature());

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisMalformed() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        String invalidToken = token.substring(1);
        boolean isTokenValid = tokenProvider.validateToken(invalidToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisExpired() {
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", -ONE_MINUTE);

        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        boolean isTokenValid = tokenProvider.validateToken(token);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisUnsupported() {
        String unsupportedToken = createUnsupportedToken();

        boolean isTokenValid = tokenProvider.validateToken(unsupportedToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisInvalid() {
        boolean isTokenValid = tokenProvider.validateToken("");

        assertThat(isTokenValid).isEqualTo(false);
    }

    private Authentication createAuthentication() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        return new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities);
    }

    private String createUnsupportedToken() {
        return Jwts.builder()
            .setPayload("payload")
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private String createTokenWithDifferentSignature() {
        Key otherKey = Keys.hmacShaKeyFor(Decoders.BASE64
            .decode("Xfd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8"));

        return Jwts.builder()
            .setSubject("anonymous")
            .signWith(otherKey, SignatureAlgorithm.HS512)
            .setExpiration(new Date(new Date().getTime() + ONE_MINUTE))
            .compact();
    }
}
