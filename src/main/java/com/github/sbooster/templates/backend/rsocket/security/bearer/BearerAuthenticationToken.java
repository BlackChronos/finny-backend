package com.github.sbooster.templates.backend.rsocket.security.bearer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class BearerAuthenticationToken implements Authentication {
    private final UserDetails userDetails;
    private final String token;
    private boolean authenticated;

    public BearerAuthenticationToken(UserDetails userDetails, String token) {
        this.userDetails = userDetails;
        this.token = token;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public UserDetails getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
