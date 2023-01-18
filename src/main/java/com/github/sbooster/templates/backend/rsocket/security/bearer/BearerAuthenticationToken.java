package com.github.sbooster.templates.backend.rsocket.security.bearer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class BearerAuthenticationToken implements Authentication {
    private final UserDetails principal;
    private final String credentials;
    private boolean authenticated;

    public BearerAuthenticationToken(UserDetails userDetails, String token) {
        this.principal = userDetails;
        this.credentials = token;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.principal.getAuthorities();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return this.principal.getUsername();
    }
}
