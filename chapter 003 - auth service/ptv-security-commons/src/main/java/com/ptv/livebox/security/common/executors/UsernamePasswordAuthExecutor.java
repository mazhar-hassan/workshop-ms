package com.ptv.livebox.security.common.executors;

import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.data.SecurityData;
import com.ptv.livebox.security.common.token.AccessToken;
import com.ptv.livebox.security.common.token.UsernamePasswordToken;
import com.ptv.livebox.security.common.utils.AuthResponseWriter;
import com.ptv.livebox.security.common.utils.JwtUtils;
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
import java.security.PrivateKey;

@Component
public class UsernamePasswordAuthExecutor implements AuthExecutor {

    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthExecutor.class);
    private final AuthenticationManager authenticationManager;
    private final PrivateKey privateKey;
    private final Long jwtExpiration;

    public UsernamePasswordAuthExecutor(AuthenticationManager authenticationManager, PrivateKey privateKey, Long jwtExpiration) {
        this.authenticationManager = authenticationManager;
        this.privateKey = privateKey;
        this.jwtExpiration = jwtExpiration;
    }

    @Override
    public void execute(SecurityData security,
                        HttpServletRequest httpRequest,
                        HttpServletResponse httpResponse) throws IOException {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordToken(security.getUsername(),
                        security.getPassword(),
                        security.getApiKey(),
                        security.getApiSecret(),
                        security.getPassword()));

        if (auth == null || !auth.isAuthenticated()) {
            logger.debug("authentication [[[failed]]]]");
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }
        logger.debug("[Auth success] - setting security context");
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthenticatedUser user = ((AccessToken) auth).getUser();
        String jwtToken = JwtUtils.createAsymmetricJWT(user,
                jwtExpiration,
                privateKey);

        AuthResponseWriter.writeSuccess(jwtToken, auth, httpRequest, httpResponse);
    }

}
