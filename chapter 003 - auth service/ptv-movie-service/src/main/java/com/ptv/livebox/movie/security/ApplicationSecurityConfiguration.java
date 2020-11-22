package com.ptv.livebox.movie.security;

import com.ptv.livebox.security.common.config.AbstractSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.ptv.livebox.security.common.data.AuthenticatedUser.DEFAULT_ROLE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class ApplicationSecurityConfiguration extends AbstractSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfiguration.class);


    @Bean
    protected AuthFilter getFilter() throws Exception {
        return new AuthFilter(authenticationManager());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .authenticationProvider(new MicroServiceAuthProvider());
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
        http.addFilterBefore(getFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return getCorsConfiguration();
    }
}
