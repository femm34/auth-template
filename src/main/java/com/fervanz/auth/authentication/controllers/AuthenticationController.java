package com.fervanz.auth.authentication.controllers;

import com.fervanz.auth.authentication.services.IAuthenticationService;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import com.fervanz.auth.shared.constants.Api;
import com.fervanz.auth.shared.payload.GlobalResponse;
import com.fervanz.auth.shared.utils.ResponseGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Api.V1_ROUTE + Api.AUTH_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @RequestMapping("/sign-up")
    ResponseEntity<GlobalResponse> signUp(@RequestBody @Valid ClientRequest clientRequest) {
        ClientResponse clientSaved = authenticationService.signUp(clientRequest);
        return ResponseGenerator.generateResponse("Client was successfully saved", clientSaved, HttpStatus.CREATED, 201);
    }
}
