package com.github.sbooster.templates.backend.core.account.model;

import org.springframework.security.core.GrantedAuthority;

// TODO Configure roles
public enum AccountType implements GrantedAuthority {
    USER,
    MODERATOR,
    ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
