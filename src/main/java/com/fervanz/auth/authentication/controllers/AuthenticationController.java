package com.fervanz.auth.authentication.controllers;

import com.fervanz.auth.authentication.dto.request.ChangePasswordRequest;
import com.fervanz.auth.authentication.dto.request.LoginRequest;
import com.fervanz.auth.authentication.dto.request.RequestResetPasswordRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping(Api.V1_ROUTE + Api.AUTH_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final HandlerMapping resourceHandlerMapping;

//    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/check")
    ResponseEntity<GlobalResponse> healthCheck() {
        return ResponseGenerator.generateResponse("Authentication service is up, running and protected", null, HttpStatus.OK, 200);
    }

    @PostMapping("/sign-up")
    ResponseEntity<GlobalResponse> signUp(@RequestBody @Valid ClientRequest clientRequest) {
        ClientResponse clientSaved = authenticationService.signUp(clientRequest);
        return ResponseGenerator.generateResponse("Client was successfully saved", clientSaved, HttpStatus.CREATED, 201);
    }

    @PostMapping("/sign-in")
    ResponseEntity<GlobalResponse> signIn(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse servletResponse) {
        LoginResponse response = authenticationService.signIn(loginRequest, servletResponse);
        return ResponseGenerator.generateResponse("Client was successfully authenticated", response, HttpStatus.OK, 200);
    }

    @PostMapping("/request-password-reset")
    ResponseEntity<GlobalResponse> requestPasswordReset(@RequestBody @Valid RequestResetPasswordRequest clientRequest) {
        authenticationService.requestPasswordReset(clientRequest);
        return ResponseGenerator.generateResponse("Password reset request was successfully sent", null, HttpStatus.OK, 200);
    }

    @PostMapping("/reset-password")
    ResponseEntity<GlobalResponse> resetPassword(@RequestParam("token") String token, @RequestBody @Valid ChangePasswordRequest clientRequest) {
        authenticationService.changePassword(token, clientRequest);
        return ResponseGenerator.generateResponse("Password reset request was successfully sent", null, HttpStatus.OK, 200);
    }

    @PostMapping("/refresh-token")
    ResponseEntity<GlobalResponse> refreshToken(@RequestParam("refreshToken") String refreshToken, HttpServletResponse httpServletResponse) {
        LoginResponse response = authenticationService.refreshToken(refreshToken, httpServletResponse);
        return ResponseGenerator.generateResponse("Refresh token was successfully generated", response, HttpStatus.OK, 200);
    }

}
