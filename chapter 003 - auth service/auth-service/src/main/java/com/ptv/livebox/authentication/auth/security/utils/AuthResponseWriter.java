package com.ptv.livebox.authentication.auth.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.authentication.auth.security.AuthException;
import com.ptv.livebox.authentication.auth.security.AuthFilter;
import com.ptv.livebox.authentication.auth.security.token.SystemAccessToken;
import com.ptv.livebox.authentication.config.ApplicationSecurityConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AuthResponseWriter {
    private static final int SECURITY_ERROR_GENERAL_EXCEPTION = 4003;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

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
        httpResponse.getWriter().write(mapper.writeValueAsString(((SystemAccessToken) auth).getUser()));
        httpResponse.addHeader(ApplicationSecurityConfiguration.HEADER_SECURITY_TOKEN, jwtToken);
        httpResponse.addHeader("auth-type", "credentials");
    }
}
