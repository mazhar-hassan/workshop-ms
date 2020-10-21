package com.ptv.livebox.authentication.auth.security;

import com.ptv.livebox.authentication.auth.security.utils.AuthResponseWriter;
import com.ptv.livebox.authentication.auth.security.utils.HTTPUtils;
import com.ptv.livebox.authentication.auth.security.utils.RequestMatcher;
import com.ptv.livebox.authentication.auth.security.executors.CredentialAuthenticationExecutor;
import com.ptv.livebox.authentication.auth.security.executors.TokenAuthenticationExecutor;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends GenericFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final CredentialAuthenticationExecutor credentialAuthenticationExecutor;
    private final TokenAuthenticationExecutor tokenAuthenticationExecutor;

    public AuthFilter(AuthenticationManager authenticationManager, SystemSettings systemSettings) {
        credentialAuthenticationExecutor = new CredentialAuthenticationExecutor(authenticationManager, systemSettings);
        tokenAuthenticationExecutor = new TokenAuthenticationExecutor(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        SecurityData data = HTTPUtils.getSecurityData(httpRequest);
        try {
            if (RequestMatcher.isAuthRequest(httpRequest, HTTPUtils.getPath(httpRequest))) {
                credentialAuthenticationExecutor.execute(data, httpRequest, httpResponse);
                return;
            } else if (RequestMatcher.isCookieAuthRequest(httpRequest, HTTPUtils.getPath(httpRequest))) {
                logger.error("Cookie authentication is not implemented yet");
                return;
            } else if (data.getToken() != null) {
                tokenAuthenticationExecutor.execute(data, httpRequest, httpResponse);
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
