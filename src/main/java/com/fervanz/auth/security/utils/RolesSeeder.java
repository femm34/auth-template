package com.fervanz.auth.security.utils;

import com.fervanz.auth.security.models.dao.RoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RolesSeeder implements CommandLineRunner {
    private final RoleDao roleDao;

    @Override
    public void run(String... args) throws Exception {
        // Logic to seed roles into the database
        // This could involve checking if roles already exist and creating them if they don't
        // For example:
        // roleRepository.save(new Role(RoleEnum.ADMIN));
        // roleRepository.save(new Role(RoleEnum.CLIENT));
    }
}
