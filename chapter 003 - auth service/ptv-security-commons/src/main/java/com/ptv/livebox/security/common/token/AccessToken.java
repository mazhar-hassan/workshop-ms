package com.ptv.livebox.security.common.token;

import com.ptv.livebox.security.common.data.AuthenticatedUser;
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
public class AccessToken extends PreAuthenticatedAuthenticationToken {
    private static final long serialVersionUID = 1L;

    private AuthenticatedUser user;

    public AccessToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    public AccessToken(Object aPrincipal, Object aCredentials,
                       Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String) getDetails();
    }

    public AuthenticatedUser getUser() {
        return user;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

}
