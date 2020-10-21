package com.ptv.livebox.authentication.auth.security.executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.authentication.auth.security.ApplicationSession;
import com.ptv.livebox.authentication.auth.security.SecurityData;
import com.ptv.livebox.authentication.auth.security.SystemSettings;
import com.ptv.livebox.authentication.auth.security.token.SystemAccessToken;
import com.ptv.livebox.authentication.auth.security.token.SystemCredentialsToken;
import com.ptv.livebox.authentication.auth.security.utils.AuthResponseWriter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class CredentialAuthenticationExecutor implements AuthExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CredentialAuthenticationExecutor.class);
    private final AuthenticationManager authenticationManager;
    private final SystemSettings settings;

    public CredentialAuthenticationExecutor(AuthenticationManager authenticationManager, SystemSettings settings) {
        this.authenticationManager = authenticationManager;
        this.settings = settings;
    }

    @Override
    public void execute(SecurityData security, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        Authentication auth = authenticationManager.authenticate(new SystemCredentialsToken(security.getUsername(),
                security.getPassword(), security.getApiKey(), security.getApiSecret(), security.getPassword()));

        if (auth == null || !auth.isAuthenticated()) {
            logger.debug("authentication [[[failed]]]]");
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }

        logger.debug("authentication [[[success]]]]");
        SecurityContextHolder.getContext().setAuthentication(auth);

        ApplicationSession session = ((SystemAccessToken) auth).getUser();

        String jwtToken = createJWT(session);

        AuthResponseWriter.writeSuccess(jwtToken, auth, httpRequest, httpResponse);
    }

    protected String createJWT(ApplicationSession session) throws IOException {
        ZonedDateTime expirationTimeUTC =
                ZonedDateTime.now(ZoneOffset.UTC).plus(settings.getJwtExpiration(), ChronoUnit.MILLIS);
        ObjectMapper mapper = new ObjectMapper();

        return Jwts.builder().setSubject(mapper.writeValueAsString(session))
                .setExpiration(Date.from(expirationTimeUTC.toInstant()))
                .signWith(SignatureAlgorithm.HS256, settings.getJwtSecretKey()).compact();
    }
}
