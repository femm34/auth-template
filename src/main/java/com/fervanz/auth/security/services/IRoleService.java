package com.fervanz.auth.security.services;

import com.fervanz.auth.security.models.entities.CustomRole;
import com.fervanz.auth.security.models.enums.RoleEnum;

import java.util.Set;

public interface IRoleService {

    Set<CustomRole> toCustomRoles(Set<String> roles);
}
