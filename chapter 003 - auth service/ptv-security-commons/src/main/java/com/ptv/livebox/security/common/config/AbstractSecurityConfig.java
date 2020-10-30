package com.ptv.livebox.security.common.config;

import com.ptv.livebox.security.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSecurityConfig.class);

    public static final String TOKEN_ERROR_MESSAGE_FOR_USERS = "Invalid token provided";

    public static final int SECURITY_ERROR_GENERAL_EXCEPTION = 40000;
    public static final int SECURITY_ERROR_EMPTY_TOKEN = 40001;
    public static final int SECURITY_ERROR_MALFORMED_TOKEN = 40002;
    public static final int SECURITY_ERROR_EXPIRED_TOKEN = 40003;
    public static final int SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION = 40004;
    public static final int SECURITY_ERROR_BAD_CREDENTIALS = 40005;
    public static final int SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME = 40006;

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

    protected void setAuthFilter(HttpSecurity http, OncePerRequestFilter filter) {
        http.addFilterBefore(filter, BasicAuthenticationFilter.class);
    }

    protected static KeyPair loadKeys(File resourceFile, String alias, String password) {

            try {
                return SecurityUtils.loadFromPKCS12(alias, resourceFile, password.toCharArray());
            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException
                    | IOException e) {
                logger.error("***************************************************************************");
                logger.error("***************************************************************************");
                logger.error("Bad Error: System not able to load keystore hence cannot continue ");
                logger.error("Exception in loading keystore", e);
                System.exit(-1);
        }
        return null;
    }

    private List<String> List_of(String ...values) {
        return Arrays.asList(values);
    }

    public CorsConfigurationSource getCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List_of("*"));
        configuration.setAllowedMethods(List_of("*"));
        configuration.setAllowCredentials(true);
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add(HEADER_SECURITY_TOKEN);
        allowedHeaders.add("auth-type");
        allowedHeaders.add("Access-Control-Expose-Headers");
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowedHeaders(List_of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
