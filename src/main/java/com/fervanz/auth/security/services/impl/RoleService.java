package com.fervanz.auth.security.services.impl;

import com.fervanz.auth.security.models.dao.RoleDao;
import com.fervanz.auth.security.models.entities.CustomRole;
import com.fervanz.auth.security.services.IRoleService;
import com.fervanz.auth.shared.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleDao roleDao;

    @Override
    public Set<CustomRole> toCustomRoles(Set<String> roles) {
        return roles.stream()
                .map(roleString -> roleDao.findByNameString(roleString)
                        .orElseThrow(() -> new GlobalException("Role '" + roleString + "' was not found")))
                .collect(Collectors.toSet());
    }
}
