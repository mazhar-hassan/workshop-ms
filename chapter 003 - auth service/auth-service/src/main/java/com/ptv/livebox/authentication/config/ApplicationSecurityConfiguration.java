package com.ptv.livebox.authentication.config;

import com.ptv.livebox.authentication.auth.UserAuthenticationService;
import com.ptv.livebox.authentication.auth.security.AuthFilter;
import com.ptv.livebox.authentication.auth.security.SystemSettings;
import com.ptv.livebox.authentication.auth.security.provider.CredentialsAuthenticationProvider;
import com.ptv.livebox.authentication.auth.security.provider.TokenAuthProvider;
import com.ptv.livebox.authentication.auth.security.utils.SecurityUtils;
import com.ptv.livebox.authentication.sec.AbstractSecurityConfig;
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
public class ApplicationSecurityConfiguration extends AbstractSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfiguration.class);

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

        SecurityConfiguration.create(http)
                .disableFrameOptions()
                .disableCsrf()
                .disableSession();

        /* Configure url mappings */
        http
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
        setAuthFilter(http, getFilter());


        File file = ResourceUtils.getFile("classpath:ms-workshop-PKCS-12.p12");
        loadKeys(file, settings.getKeystoreAlias(), settings.getKeystorePassword());

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return getCorsConfiguration();
    }
}
