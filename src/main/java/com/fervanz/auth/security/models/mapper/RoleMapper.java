package com.fervanz.auth.security.models.mapper;

import com.fervanz.auth.security.models.entities.CustomRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class RoleMapper {
    public static Collection<GrantedAuthority> ToGrantedAuthorities(Set<CustomRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toUnmodifiableList());
    }


    public static List<String> toString(Set<CustomRole> roles) {
        return roles == null
                ? Collections.emptyList()
                : roles.stream()
                .map(role -> role.getName().name())
                .toList();
    }

}
