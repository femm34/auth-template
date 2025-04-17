package com.fervanz.auth.security.utils;

import com.fervanz.auth.security.models.dao.RoleDao;
import com.fervanz.auth.security.models.entities.CustomRole;
import com.fervanz.auth.security.models.enums.RoleEnum;
import com.fervanz.auth.shared.exceptions.GlobalException;
import com.fervanz.auth.shared.models.dao.ConfigurationEntityDao;
import com.fervanz.auth.shared.models.entities.ConfigurationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RolesSeeder implements CommandLineRunner {
    private final RoleDao roleDao;
    private final ConfigurationEntityDao configurationEntityDao;
    private final String ROLE_KEY = "ROLES_LOADED";

    @Override
    public void run(String... args) throws GlobalException {
        configurationEntityDao.findByKey(ROLE_KEY)
                .ifPresentOrElse(
                        config -> {
                            if (config.getValue().equals("false")) {
                                loadRoles();
                                config.setValue("true");
                                configurationEntityDao.save(config);
                            }
                        },
                        () -> {
                            loadRoles();
                            configurationEntityDao.save(ConfigurationEntity.builder()
                                    .key(ROLE_KEY)
                                    .value("true")
                                    .build());
                        }
                );

    }

    private void loadRoles() {
        RoleEnum[] roles = RoleEnum.values();
        List<CustomRole> rolesList = Arrays.stream(roles)
                .map(role -> CustomRole.builder()
                        .name(role)
                        .createdBy("system")
                        .active(true)
                        .build())
                .collect(Collectors.toList());

        roleDao.saveAll(rolesList);
    }
}
