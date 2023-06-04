package com.gihub.blackchronos.finny.rsocket.security.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface StoredUserDetails extends UserDetails {
    Long getId();

    boolean isLocked();

    @Override
    default boolean isAccountNonLocked() {
        return !this.isLocked();
    }

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }
}
