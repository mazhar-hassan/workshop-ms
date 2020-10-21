package com.ptv.livebox.authentication.auth.security.executors;

import com.ptv.livebox.authentication.auth.security.SecurityData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthenticationExecutor implements AuthExecutor {
    private final AuthenticationManager authenticationManager;

    public TokenAuthenticationExecutor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void execute(SecurityData security, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        Authentication auth = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(security.getToken(), null));
        if (auth == null || !auth.isAuthenticated()) {
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
