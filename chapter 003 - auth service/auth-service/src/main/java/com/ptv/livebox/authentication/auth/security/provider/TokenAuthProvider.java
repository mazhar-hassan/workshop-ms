
package com.ptv.livebox.authentication.auth.security.provider;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.authentication.auth.security.AuthException;
import com.ptv.livebox.authentication.auth.security.ApplicationSession;
import com.ptv.livebox.authentication.auth.security.SystemSettings;
import com.ptv.livebox.authentication.auth.security.token.SystemAccessToken;
import com.ptv.livebox.authentication.config.ApplicationSecurityConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public class TokenAuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthProvider.class);
    private static final String TOKEN_ERROR_MESSAGE_FOR_USERS = "Invalid token provided";

    SystemSettings settings;

    public TokenAuthProvider(SystemSettings settings) {
        this.settings = settings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String jwtToken = (String) authentication.getPrincipal();
        verifyNonNull(jwtToken);
        String jwtJson = extractJsonFromToken(jwtToken);
        ApplicationSession session = extractSessionDetail(jwtJson);
        validateToken(session);
        return createPreAuthenticatedToken(session);
    }

    private void verifyNonNull(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            logger.error("Empty token was passed");
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    ApplicationSecurityConfiguration.SECURITY_ERROR_EMPTY_TOKEN);
        }
    }

    private ApplicationSession extractSessionDetail(String jwtJson) {
        ObjectMapper mapper = new ObjectMapper();
        ApplicationSession session;
        try {
            session = mapper.readValue(jwtJson, ApplicationSession.class);
        } catch (IOException e) {
            logger.error("Unable to create object from jwt's JSON", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    ApplicationSecurityConfiguration.SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION);
        }
        return session;
    }

    private String extractJsonFromToken(String jwtToken) {
        String jwtJson = null;
        try {
            jwtJson =
                    Jwts.parser()
                            .setSigningKey(settings.getJwtSecretKey())
                            .parseClaimsJws(jwtToken)
                            .getBody()
                            .getSubject();

        } catch (MalformedJwtException e) {
            logger.error("Malformed: Invalid token used", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    ApplicationSecurityConfiguration.SECURITY_ERROR_MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            logger.error("Expired: Invalid token used", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    ApplicationSecurityConfiguration.SECURITY_ERROR_EXPIRED_TOKEN);
        }
        return jwtJson;
    }

    private void validateToken(ApplicationSession session) {
        if (!(session instanceof ApplicationSession)) {
            logger.error("Token validation failed");
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    ApplicationSecurityConfiguration.SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION);
        }
        // an in-memory db call can be made to verify if this is our token
        // as this token is decrypted successfully hence we can assume its valid token
        // for this assignment only (Not advised for production)
    }

    private SystemAccessToken createPreAuthenticatedToken(ApplicationSession session) {
        SystemAccessToken authResponse = new SystemAccessToken(session.getUsername(), "******",
                session.toAuthorities());
        authResponse.setUser(session);

        return authResponse;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
