package com.ptv.livebox.movie.security;

import com.ptv.livebox.security.common.exception.AuthException;
import com.ptv.livebox.security.common.token.AccessToken;
import com.ptv.livebox.security.common.utils.AuthResponseWriter;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private final AuthenticationManager authenticationManager;

    public AuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {

            String userInfo = request.getHeader("user");
            if (userInfo == null) {
                throw new SecurityException("Microservice user info is missing");
            }

            Authentication auth = authenticationManager.authenticate(new AccessToken(userInfo, (Object) null));
            if (auth != null && auth.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
            }


            chain.doFilter(request, response);
        } catch (AuthException | AuthenticationException | GenericJDBCException exception) {
            logRequest(exception, request);
            AuthResponseWriter.writeException(response, exception.getMessage(), exception);
        }
    }

    public void logRequest(Exception e, HttpServletRequest httpRequest) {
        logger.error("Security exception occurred: ", e);
    }
}
