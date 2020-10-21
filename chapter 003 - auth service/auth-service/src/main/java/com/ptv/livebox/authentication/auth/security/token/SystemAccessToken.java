package com.ptv.livebox.authentication.auth.security.token;

import com.ptv.livebox.authentication.auth.security.ApplicationSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public class SystemAccessToken extends PreAuthenticatedAuthenticationToken {
    private static final long serialVersionUID = 1L;

    private ApplicationSession user;

    public SystemAccessToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    public SystemAccessToken(Object aPrincipal, Object aCredentials,
                             Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String) getDetails();
    }

    public ApplicationSession getUser() {
        return user;
    }

    public void setUser(ApplicationSession user) {
        this.user = user;
    }

}
