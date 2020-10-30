package com.ptv.livebox.security.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptv.livebox.security.common.exception.AuthException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.ptv.livebox.security.common.config.AbstractSecurityConfig.*;

public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public String createJWT(Object user, Long expiration, String symmetricKey) throws IOException {

        return getJwtBuilder(user, expiration)
                .signWith(SignatureAlgorithm.HS256, symmetricKey).compact();
    }

    public static String createAsymmetricJWT(Object user, Long expiration, PrivateKey privateKey) throws IOException {

        return getJwtBuilder(user, expiration)
                .signWith(SignatureAlgorithm.RS512, privateKey).compact();
    }

    public static String extractJSON(String jwtToken, PublicKey publicKey) {
        String jwtJson = null;
        try {
            jwtJson =
                    Jwts.parser()
                            .setSigningKey(publicKey)
                            .parseClaimsJws(jwtToken)
                            .getBody()
                            .getSubject();

        } catch (MalformedJwtException e) {
            logger.error("Malformed: Invalid token used", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    SECURITY_ERROR_MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            logger.error("Expired: Invalid token used", e);
            throw new AuthException(TOKEN_ERROR_MESSAGE_FOR_USERS,
                    SECURITY_ERROR_EXPIRED_TOKEN);
        }

        return jwtJson;
    }

    private static JwtBuilder getJwtBuilder(Object user, Long expiration) throws JsonProcessingException {
        return Jwts.builder().setSubject(mapper.writeValueAsString(user))
                .setExpiration(Date.from(getZonedDateTime(expiration).toInstant()));
    }

    private static ZonedDateTime getZonedDateTime(Long expiration) {
        return ZonedDateTime.now(ZoneOffset.UTC).plus(expiration, ChronoUnit.MILLIS);
    }
}
