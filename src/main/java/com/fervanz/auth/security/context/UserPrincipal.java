package com.fervanz.auth.security.context;

import com.fervanz.auth.client.models.entities.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Client client;


    @Override
    public String getUsername() {
        return client.getUsername();
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !client.isAccountLocked() && client.getLockTime() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return client.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
