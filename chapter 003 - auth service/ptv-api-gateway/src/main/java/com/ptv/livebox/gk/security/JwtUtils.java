package com.ptv.livebox.gk.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;

public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();


    public static String extractJSON(String jwtToken, PublicKey publicKey) {
        String jwtJson = null;

        try {
            jwtJson = ((Claims) Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwtToken)
                    .getBody()).getSubject();

            return jwtJson;
        } catch (MalformedJwtException var4) {
            logger.error("Malformed: Invalid token used", var4);
            throw new SecurityException("Invalid for malformed token provided");
        } catch (ExpiredJwtException var5) {
            logger.error("Expired: Invalid token used", var5);
            throw new SecurityException("Invalid or expired token");
        }
    }
}
