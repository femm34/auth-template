package com.fervanz.auth.authentication.services.impl;

import com.fervanz.auth.authentication.services.IAuthenticationService;
import com.fervanz.auth.client.models.dao.ClientDao;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.client.models.mappers.ClientMapper;
import com.fervanz.auth.shared.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;

    private boolean existsBy(String parameter, Function<String, Boolean> existsFunction) {
        return existsFunction.apply(parameter);
    }

    private void existAny(boolean usernameExists, boolean emailExists) {
        if (usernameExists && emailExists) {
            log.error("Username and Email already exists");
            throw new GlobalException("Username and Email already exists");
        }
        if (usernameExists) {
            log.error("Username already exists");
            throw new GlobalException("Username already exists");
        }
        if (emailExists) {
            log.error("Email Already exists");
            throw new GlobalException("Email already exists");
        }
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            log.error("Password and Confirm Password do not match");
            throw new GlobalException("Password and Confirm Password do not match");
        }
    }

    @Override
    public ClientResponse signUp(ClientRequest clientResponse) {
        boolean existsByUsername = existsBy(clientResponse.getUsername(), clientDao::existsByUsername);
        boolean existsByEmail = existsBy(clientResponse.getEmail(), clientDao::existsByEmail);

        validatePassword(clientResponse.getPassword(), clientResponse.getConfirmPassword());
        existAny(existsByUsername, existsByEmail);

        Client clientSaved = clientDao.save(clientMapper.toEntity(clientResponse));
        return clientMapper.toResponse(clientSaved);
    }
}
