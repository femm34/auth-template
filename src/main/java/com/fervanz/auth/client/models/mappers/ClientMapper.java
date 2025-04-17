package com.fervanz.auth.client.models.mappers;

import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.security.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientMapper {
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Client toEntity(ClientRequest clientRequest) {
        return Client.builder()
                .username(clientRequest.getUsername())
                .email(clientRequest.getEmail())
                .name(clientRequest.getName())
                .motherSurname(clientRequest.getMotherSurname())
                .password(passwordEncoder.encode(clientRequest.getPassword()))
                .fatherSurname(clientRequest.getFatherSurname())
                .roles(roleService.toCustomRoles(clientRequest.getRoles()))
                .createdBy(clientRequest.getUsername())
                .build();
    }

    public ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .username(client.getUsername())
                .email(client.getEmail())
                .name(client.getName())
                .motherSurname(client.getMotherSurname())
                .fatherSurname(client.getFatherSurname())
                .build();
    }
}
