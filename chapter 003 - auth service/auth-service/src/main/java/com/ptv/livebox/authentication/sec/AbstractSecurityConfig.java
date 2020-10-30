package com.ptv.livebox.authentication.sec;

import com.ptv.livebox.authentication.auth.security.AuthFilter;
import com.ptv.livebox.authentication.auth.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSecurityConfig.class);

    public static final int SECURITY_ERROR_EMPTY_TOKEN = 40001;
    public static final int SECURITY_ERROR_MALFORMED_TOKEN = 40002;
    public static final int SECURITY_ERROR_EXPIRED_TOKEN = 40003;
    public static final int SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION = 40004;
    public static final int SECURITY_ERROR_BAD_CREDENTIALS = 40005;
    public static final int SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME = 40006;
    public static KeyPair keypair = null;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_SECURITY_TOKEN = "Authorization";
    public static final String URL_AUTH = "/auth/login";
    public static final String URL_AUTH_COOKIE = "/auth/cookie";

    public static class SecurityConfiguration {

        HttpSecurity httpSecurity;

        public static SecurityConfiguration create(HttpSecurity httpSecurity) {
            SecurityConfiguration configuration = new SecurityConfiguration();
            configuration.httpSecurity = httpSecurity;
            return configuration;
        }
        public SecurityConfiguration disableFrameOptions() throws Exception {
            /* X- Frame issue */
            httpSecurity
                    .headers()
                    .frameOptions().disable();
            return this;
        }

        public SecurityConfiguration disableCsrf() throws Exception {
            httpSecurity
                    .csrf()
                    .disable();

            return this;
        }

        public SecurityConfiguration disableSession() throws Exception {
            httpSecurity
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            return this;
        }
    }

    public void addRole() {

    }

    protected void setAuthFilter(HttpSecurity http, AuthFilter filter) {
        http.addFilterBefore(filter, BasicAuthenticationFilter.class);
    }

    protected static void loadKeys(File resourceFile, String alias, String password) {
        if (keypair == null) {
            try {
                keypair = SecurityUtils.loadFromPKCS12(alias, resourceFile, password.toCharArray());
            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException
                    | IOException e) {
                logger.error("***************************************************************************");
                logger.error("***************************************************************************");
                logger.error("Bad Error: System not able to load keystore hence cannot continue ");
                logger.error("Exception in loading keystore", e);
                System.exit(-1);

            }
        }
    }

    public CorsConfigurationSource getCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add(HEADER_SECURITY_TOKEN);
        allowedHeaders.add("auth-type");
        allowedHeaders.add("Access-Control-Expose-Headers");
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}