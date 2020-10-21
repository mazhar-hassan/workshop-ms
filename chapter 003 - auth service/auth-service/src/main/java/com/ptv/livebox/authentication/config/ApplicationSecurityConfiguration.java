package com.ptv.livebox.authentication.config;

import com.ptv.livebox.authentication.auth.UserAuthenticationService;
import com.ptv.livebox.authentication.auth.security.AuthFilter;
import com.ptv.livebox.authentication.auth.security.SystemSettings;
import com.ptv.livebox.authentication.auth.security.provider.CredentialsAuthenticationProvider;
import com.ptv.livebox.authentication.auth.security.provider.TokenAuthProvider;
import com.ptv.livebox.authentication.auth.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ResourceUtils;
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

import static com.ptv.livebox.authentication.auth.security.ApplicationSession.DEFAULT_ROLE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final int SECURITY_ERROR_EMPTY_TOKEN = 40001;
    public static final int SECURITY_ERROR_MALFORMED_TOKEN = 40002;
    public static final int SECURITY_ERROR_EXPIRED_TOKEN = 40003;
    public static final int SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION = 40004;
    public static final int SECURITY_ERROR_BAD_CREDENTIALS = 40005;
    public static final int SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME = 40006;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthProvider.class);

    public static KeyPair keypair = null;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_SECURITY_TOKEN = "Authorization";
    public static final String URL_AUTH = "/auth/login";
    public static final String URL_AUTH_COOKIE = "/auth/cookie";

    @Autowired
    private UserAuthenticationService userDetailService;

    @Autowired
    SystemSettings settings;

    @Bean
    protected AuthFilter getFilter() throws Exception {
        return new AuthFilter(authenticationManager(), settings);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(new CredentialsAuthenticationProvider(userDetailService))
                .authenticationProvider(new TokenAuthProvider(settings));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* X- Frame issue */
        http
                .headers()
                .frameOptions().disable();
        /* Configure url mappings */
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .hasAnyRole(DEFAULT_ROLE)
                .antMatchers("/", "/css/*", "/js/*", "/h2/**")
                .permitAll();

        /* Configure cross origin */
        http
                .cors()
                .configurationSource(corsConfigurationSource());

        /* Configure auth scheme */
        http
                .httpBasic();

        /* Configure Security Filter - A life changer */
        http.addFilterBefore(getFilter(), BasicAuthenticationFilter.class);

        File file = ResourceUtils.getFile("classpath:ms-workshop-PKCS-12.p12");
        loadKeys(file, settings.getKeystoreAlias(), settings.getKeystorePassword());

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
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


    private static void loadKeys(File resourceFile, String alias, String password) {
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
}
