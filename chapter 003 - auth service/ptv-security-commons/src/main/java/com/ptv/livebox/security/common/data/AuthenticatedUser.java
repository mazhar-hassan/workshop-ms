package com.ptv.livebox.security.common.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthenticatedUser {
    public static final String DEFAULT_ROLE = "PTV_AUTHENTICATED";

    private Integer id;
    private String username;
    private List<AutheticatedUserRole> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<AutheticatedUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AutheticatedUserRole> roles) {
        this.roles = roles;
    }

    public Collection<? extends GrantedAuthority> toAuthorities() {

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (null != roles) {
            roles.forEach(role -> grantedAuthorities.add(mapRole(role)));
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(mapRole(DEFAULT_ROLE)));

        return grantedAuthorities;
    }

    private SimpleGrantedAuthority mapRole(AutheticatedUserRole role) {
        return new SimpleGrantedAuthority(mapRole(role.getRoleName()));
    }

    private String mapRole(String roleName) {
        StringBuilder role = new StringBuilder("ROLE_");
        role.append(roleName.toUpperCase());

        return role.toString();
    }
}
