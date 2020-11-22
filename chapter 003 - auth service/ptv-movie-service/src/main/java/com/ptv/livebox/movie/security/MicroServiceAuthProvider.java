package com.ptv.livebox.movie.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.token.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

public class MicroServiceAuthProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(MicroServiceAuthProvider.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public Authentication authenticate(Authentication authentication) {

        String jwtToken = (String) authentication.getPrincipal();

        return createAuth(jwtToken);
    }

    private Authentication createAuth(String json) {
        try {
            AuthenticatedUser user = mapper.readValue(json, AuthenticatedUser.class);

            AccessToken token = new AccessToken(user.getUsername(), "****", user.toAuthorities());
            token.setUser(user);
            token.setAuthenticated(true);
            //authenticationSuccess(user);
            return token;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //protected abstract void authenticationSuccess(AuthenticatedUser user);


    public boolean supports(Class<?> authentication) {
        return authentication.equals(AccessToken.class);
    }
}