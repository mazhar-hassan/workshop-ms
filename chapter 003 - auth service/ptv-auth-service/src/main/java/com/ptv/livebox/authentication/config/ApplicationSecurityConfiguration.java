package com.ptv.livebox.authentication.config;

import com.ptv.livebox.security.common.config.AbstractSecurityConfig;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.provider.TokenAuthenticationProvider;
import com.ptv.livebox.security.common.provider.UserAuthenticationService;
import com.ptv.livebox.security.common.provider.UsernamePasswordAuthenticationProvider;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.File;
import java.security.KeyPair;

import static com.ptv.livebox.security.common.data.AuthenticatedUser.DEFAULT_ROLE;

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
    private KeyPair keypair;

    @Bean
    protected AuthFilter getFilter() throws Exception {
        return new AuthFilter(authenticationManager(), keypair.getPrivate(), settings.getJwtExpiration());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        File file = ResourceUtils.getFile("classpath:ms-workshop-PKCS-12.p12");
        keypair = AbstractSecurityConfig.loadKeys(file, settings.getKeystoreAlias(), settings.getKeystorePassword());

        auth
                .authenticationProvider(new UsernamePasswordAuthenticationProvider(userDetailService))
                .authenticationProvider(new TokenAuthenticationProvider(keypair.getPublic()) {
                    @Override
                    protected void authenticationSuccess(AuthenticatedUser user) {
                        //check in memory if you want
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AbstractSecurityConfig.SecurityConfiguration.create(http)
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
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return getCorsConfiguration();
    }
}
