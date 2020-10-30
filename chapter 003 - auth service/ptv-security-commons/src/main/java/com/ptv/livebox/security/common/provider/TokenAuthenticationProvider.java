
package com.ptv.livebox.security.common.provider;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.exception.AuthException;
import com.ptv.livebox.security.common.token.AccessToken;
import com.ptv.livebox.security.common.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.PublicKey;

import static com.ptv.livebox.security.common.config.AbstractSecurityConfig.*;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public abstract class TokenAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
    private final PublicKey publicKey;

    public TokenAuthenticationProvider(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String jwtToken = (String) authentication.getPrincipal();
        verifyNonNull(jwtToken);

        String jwtJson = JwtUtils.extractJSON(jwtToken, publicKey);
        AuthenticatedUser user = toUser(jwtJson);
        validateUser(user);

        return createPreAuthenticatedToken(user);
    }

    private void verifyNonNull(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            logger.error("Empty token was passed");
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    SECURITY_ERROR_EMPTY_TOKEN);
        }
    }

    private AuthenticatedUser toUser(String jwtJson) {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticatedUser user;
        try {
            user = mapper.readValue(jwtJson, AuthenticatedUser.class);
        } catch (IOException e) {
            logger.error("Unable to create object from jwt's JSON", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION);
        }

        return user;
    }

    private void validateUser(AuthenticatedUser user) {
        if (null == user) {
            logger.error("Token validation failed");
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION);
        }

        authenticationSuccess(user);
        // an in-memory db call can be made to verify if this is our token
        // as this token is decrypted successfully hence we can assume its valid token
        // for this assignment only (Not advised for production)
    }

    protected abstract void authenticationSuccess(AuthenticatedUser user);

    private AccessToken createPreAuthenticatedToken(AuthenticatedUser user) {
        AccessToken authResponse = new AccessToken(user.getUsername(), "******",
                user.toAuthorities());
        authResponse.setUser(user);

        return authResponse;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AccessToken.class);
    }
}
