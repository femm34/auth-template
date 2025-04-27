package com.fervanz.auth.authentication.services;

import com.fervanz.auth.authentication.dto.request.ChangePasswordRequest;
import com.fervanz.auth.authentication.dto.request.LoginRequest;
import com.fervanz.auth.authentication.dto.request.RequestResetPasswordRequest;
import com.fervanz.auth.authentication.dto.response.LoginResponse;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthenticationService {
    ClientResponse signUp(ClientRequest clientRequest);
    LoginResponse signIn(LoginRequest loginRequest, HttpServletResponse response);
    void requestPasswordReset(RequestResetPasswordRequest requestResetPasswordRequest);
    void changePassword(String token, ChangePasswordRequest changePasswordRequest);
    LoginResponse refreshToken(String refreshToken, HttpServletResponse response);
}
