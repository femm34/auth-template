package com.fervanz.auth.authentication.services.impl;

import com.fervanz.auth.authentication.services.IAuthenticationService;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Override
    public ClientResponse signUp(ClientRequest clientResponse) {
        return null;
    }
}
