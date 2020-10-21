package com.ptv.livebox.authentication.auth.security.provider;

import com.ptv.livebox.authentication.auth.UserAuthenticationService;
import com.ptv.livebox.authentication.auth.security.AuthException;
import com.ptv.livebox.authentication.auth.security.ApplicationSession;
import com.ptv.livebox.authentication.auth.security.token.SystemAccessToken;
import com.ptv.livebox.authentication.auth.security.token.SystemCredentialsToken;
import com.ptv.livebox.authentication.config.ApplicationSecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public class CredentialsAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CredentialsAuthenticationProvider.class);
    UserAuthenticationService authenticationService;

    public CredentialsAuthenticationProvider(UserAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (username == null || password == null) {
            logger.error("Username or Password is null");
            throw new AuthException("Bad credentials", ApplicationSecurityConfiguration.SECURITY_ERROR_BAD_CREDENTIALS);
        }
        if (!(authentication instanceof SystemCredentialsToken)) {
            logger.error("Selected authentication scheme is not valid");
            throw new AuthException("Bad credentials",
                    ApplicationSecurityConfiguration.SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME);
        }

        ApplicationSession session = authenticationService.authenticate(username, password);

        SystemAccessToken authResponse = new SystemAccessToken(username, "******",
                session.toAuthorities());
        authResponse.setUser(session);

        return authResponse;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SystemCredentialsToken.class);
    }
}
