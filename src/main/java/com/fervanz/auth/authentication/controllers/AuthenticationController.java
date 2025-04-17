package com.fervanz.auth.authentication.controllers;

import com.fervanz.auth.authentication.dto.request.LoginRequest;
import com.fervanz.auth.authentication.dto.response.LoginResponse;
import com.fervanz.auth.authentication.services.IAuthenticationService;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import com.fervanz.auth.shared.constants.Api;
import com.fervanz.auth.shared.payload.GlobalResponse;
import com.fervanz.auth.shared.utils.ResponseGenerator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping(Api.V1_ROUTE + Api.AUTH_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final HandlerMapping resourceHandlerMapping;

    @RequestMapping("/sign-up")
    ResponseEntity<GlobalResponse> signUp(@RequestBody @Valid ClientRequest clientRequest) {
        ClientResponse clientSaved = authenticationService.signUp(clientRequest);
        return ResponseGenerator.generateResponse("Client was successfully saved", clientSaved, HttpStatus.CREATED, 201);
    }

    @RequestMapping("/sign-in")
    ResponseEntity<GlobalResponse> signIn(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse servletResponse) {
        LoginResponse response = authenticationService.signIn(loginRequest, servletResponse);
        return ResponseGenerator.generateResponse("Client was successfully authenticated", response, HttpStatus.OK, 200);
    }
}
