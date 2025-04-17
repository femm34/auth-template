package com.fervanz.auth.authentication.services;

import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;

public interface IAuthenticationService {
    ClientResponse signUp(ClientRequest clientResponse);
}
