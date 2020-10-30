package com.ptv.livebox.security.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.security.common.config.AbstractSecurityConfig;
import com.ptv.livebox.security.common.exception.AuthException;
import com.ptv.livebox.security.common.token.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ptv.livebox.security.common.config.AbstractSecurityConfig.SECURITY_ERROR_GENERAL_EXCEPTION;

public class AuthResponseWriter {

    private static final Logger logger = LoggerFactory.getLogger(AuthResponseWriter.class);

    public static void writeException(HttpServletResponse response, String message, Exception exception) throws IOException {
        SecurityContextHolder.clearContext();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        int code = SECURITY_ERROR_GENERAL_EXCEPTION;
        if (exception instanceof AuthException) {
            code = ((AuthException) exception).getCode();
        }
        response.getOutputStream().println("{ \"code\": " + code + ",\"message\": \"" + message + "\" }");
    }

    public static void writeSuccess(String jwtToken, Authentication auth, HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.addHeader("Access-Control-Allow-Headers", "Authorization");
        httpResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
        ObjectMapper mapper = new ObjectMapper();
        httpResponse.getWriter().write(mapper.writeValueAsString(((AccessToken) auth).getUser()));
        httpResponse.addHeader(AbstractSecurityConfig.HEADER_SECURITY_TOKEN, jwtToken);
        httpResponse.addHeader("auth-type", "credentials");
    }
}
