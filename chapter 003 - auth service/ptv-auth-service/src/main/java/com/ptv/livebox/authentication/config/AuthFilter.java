package com.ptv.livebox.authentication.config;

import com.ptv.livebox.security.common.data.SecurityData;
import com.ptv.livebox.security.common.exception.AuthException;
import com.ptv.livebox.security.common.executors.TokenAuthExecutor;
import com.ptv.livebox.security.common.executors.UsernamePasswordAuthExecutor;
import com.ptv.livebox.security.common.utils.AuthResponseWriter;
import com.ptv.livebox.security.common.utils.HTTPUtils;
import com.ptv.livebox.security.common.utils.RequestMatcher;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;

public class AuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final UsernamePasswordAuthExecutor usernamePasswordAuthExecutor;
    private final TokenAuthExecutor tokenAuthExecutor;

    public AuthFilter(AuthenticationManager authenticationManager, PrivateKey privateKey, Long jwtExpiration) {
        usernamePasswordAuthExecutor = new UsernamePasswordAuthExecutor(authenticationManager, privateKey, jwtExpiration);
        tokenAuthExecutor = new TokenAuthExecutor(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        SecurityData data = HTTPUtils.parseSecurityData(httpRequest);
        try {
            if (RequestMatcher.isAuthRequest(httpRequest, HTTPUtils.getPath(httpRequest))) {
                usernamePasswordAuthExecutor.execute(data, httpRequest, httpResponse);
                return;
            } else if (RequestMatcher.isCookieAuthRequest(httpRequest, HTTPUtils.getPath(httpRequest))) {
                logger.error("Cookie authentication is not implemented yet");
                return;
            } else if (data.getToken() != null) {
                tokenAuthExecutor.execute(data, httpRequest, httpResponse);
            } else {
                // public
                // logger.error("No authentication scheme selected - unauthorized");
                // logger.error(HTTPUtils.getPath(httpRequest));
            }
            chain.doFilter(request, response);
        } catch (AuthException | AuthenticationException | GenericJDBCException exception) {
            logRequest(exception, httpRequest);
            AuthResponseWriter.writeException(httpResponse, exception.getMessage(), exception);
        }
    }

    public void logRequest(Exception e, HttpServletRequest httpRequest) {
        logger.error("Security exception occurred: ", e);
    }
}
