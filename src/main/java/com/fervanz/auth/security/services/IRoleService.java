package com.fervanz.auth.security.services;

import com.fervanz.auth.security.models.entities.CustomRole;

import java.util.Set;

public interface IRoleService {

    Set<CustomRole> toCustomRoles(Set<String> roles);
}
