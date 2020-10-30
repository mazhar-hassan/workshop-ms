package com.ptv.livebox.security.common.provider;

import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.exception.AuthException;
import com.ptv.livebox.security.common.token.AccessToken;
import com.ptv.livebox.security.common.token.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static com.ptv.livebox.security.common.config.AbstractSecurityConfig.SECURITY_ERROR_BAD_CREDENTIALS;
import static com.ptv.livebox.security.common.config.AbstractSecurityConfig.SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);
    UserAuthenticationService authenticationService;

    public UsernamePasswordAuthenticationProvider(UserAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (username == null || password == null) {
            logger.error("Username or Password is null");
            throw new AuthException("Bad credentials", SECURITY_ERROR_BAD_CREDENTIALS);
        }
        if (!(authentication instanceof UsernamePasswordToken)) {
            logger.error("Selected authentication scheme is not valid");
            throw new AuthException("Bad credentials",
                    SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME);
        }

        AuthenticatedUser user = authenticationService.authenticate(username, password);
        AccessToken accessToken = new AccessToken(username, "******",
                user.toAuthorities());
        accessToken.setUser(user);

        return accessToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordToken.class);
    }
}
