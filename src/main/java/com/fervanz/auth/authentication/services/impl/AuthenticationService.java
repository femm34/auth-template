package com.fervanz.auth.authentication.services.impl;

import com.fervanz.auth.authentication.dto.request.ChangePasswordRequest;
import com.fervanz.auth.authentication.dto.request.LoginRequest;
import com.fervanz.auth.authentication.dto.request.RequestResetPasswordRequest;
import com.fervanz.auth.authentication.dto.response.LoginResponse;
import com.fervanz.auth.authentication.services.IAuthenticationService;
import com.fervanz.auth.client.models.dao.ClientDao;
import com.fervanz.auth.client.models.dto.request.ClientRequest;
import com.fervanz.auth.client.models.dto.response.ClientResponse;
import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.client.models.mappers.ClientMapper;
import com.fervanz.auth.security.context.jwt.dto.JWTResponse;
import com.fervanz.auth.security.context.jwt.enums.TokenStatus;
import com.fervanz.auth.security.context.jwt.enums.TokenType;
import com.fervanz.auth.security.context.jwt.service.IJWTService;
import com.fervanz.auth.security.models.dao.PasswordResetTokenDao;
import com.fervanz.auth.security.models.dao.RefreshTokenDao;
import com.fervanz.auth.security.models.entities.PasswordResetToken;
import com.fervanz.auth.security.models.entities.RefreshToken;
import com.fervanz.auth.shared.exceptions.GlobalException;
import com.fervanz.auth.shared.services.IEmailService;
import com.fervanz.auth.shared.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService implements IAuthenticationService {
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;
    private final RefreshTokenDao refreshTokenDao;
    private final AuthenticationManager authenticationManager;
    private final IJWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final PasswordResetTokenDao passwordResetTokenDao;
    public static final long LOCK_TIME_DURATION = 5;
    public static final int MAX_ATTEMPTS = 5;
    public static final long TOKEN_EXPIRE_TIME = 10L * 60 * 1000;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 30L * 24 * 60 * 60 * 1000;
    public static final long RESET_PASSWORD_TOKEN_TIME = 600000;

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
    public ClientResponse signUp(ClientRequest clientRequest) {
        boolean existsByUsername = existsBy(clientRequest.getUsername(), clientDao::existsByUsername);
        boolean existsByEmail = existsBy(clientRequest.getEmail(), clientDao::existsByEmail);

        validatePassword(clientRequest.getPassword(), clientRequest.getConfirmPassword());
        existAny(existsByUsername, existsByEmail);

        Client clientSaved = clientDao.save(clientMapper.toEntity(clientRequest));
        return clientMapper.toResponse(clientSaved);
    }


    @Override
    public LoginResponse signIn(LoginRequest loginRequest, HttpServletResponse response) {
        Client client = getClientByUsername(loginRequest.getUsername());

        unlockIfEligible(client);

        try {
            authenticateClient(loginRequest);
            resetFailedAttempts(client);
            return generateLoginResponse(client, response);
        } catch (BadCredentialsException e) {
            handleFailedLoginAttempt(client, loginRequest.getUsername());
            throw new GlobalException("Invalid password. Try again.");
        }
    }

    @Override
    public void changePassword(String token, ChangePasswordRequest changePasswordRequest) {
        if (token.isEmpty()) {
            throw new GlobalException("Token is empty");
        }

        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new GlobalException("Password and Confirm Password do not match");
        }

        PasswordResetToken passwordResetTokenFound = passwordResetTokenDao.findByToken(token)
                .orElseThrow(() -> new GlobalException("Token not found"));

        Client clientFound = passwordResetTokenFound.getClient();

        validatePasswordResetToken(passwordResetTokenFound, changePasswordRequest.getPassword(), clientFound);
    }

    private boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        return passwordResetToken.getExpireDate().isBefore(LocalDateTime.now());
    }

    private void validatePasswordResetToken(PasswordResetToken passwordResetTokenFound, String password, Client clientFound) {
        if (passwordResetTokenFound.getStatus().equals(TokenStatus.EXPIRED)) {
            throw new GlobalException("Token expired");
        } else if (passwordResetTokenFound.getStatus().equals(TokenStatus.USED)) {
            throw new GlobalException("Token has been used");
        } else if (isTokenExpired(passwordResetTokenFound) && passwordResetTokenFound.getStatus().equals(TokenStatus.ACTIVE)) {
            passwordResetTokenFound.setStatus(TokenStatus.EXPIRED);
            passwordResetTokenDao.save(passwordResetTokenFound);
            throw new GlobalException("Token expired");
        }
        if (!isTokenExpired(passwordResetTokenFound) && passwordResetTokenFound.getStatus().equals(TokenStatus.ACTIVE)) {
            String passwordEncoded = passwordEncoder.encode(password);
            clientFound.setPassword(passwordEncoded);
            this.clientDao.save(clientFound);

            passwordResetTokenFound.setStatus(TokenStatus.USED);
            passwordResetTokenDao.save(passwordResetTokenFound);
        }
    }

    private Client getClientByUsername(String username) {
        return clientDao.findByUsername(username)
                .orElseThrow(() -> new GlobalException("Client not found with username: " + username));
    }

    private void unlockIfEligible(Client client) {
        if (client.isAccountLocked()) {
            LocalDateTime lockTime = client.getLockTime();
            if (lockTime != null && LocalDateTime.now().isBefore(lockTime)) {
                Duration minutesLeft = Duration.between(LocalDateTime.now(), lockTime);
                log.error("Account is locked until {}", lockTime);
                throw new GlobalException("Your account is locked. Try again in " +
                        String.format("%02d:%02d", minutesLeft.toMinutes(), minutesLeft.toSeconds() % 60) + " minutes.");
            }

            client.setAccountLocked(false);
            client.setLockTime(null);
            client.setFailedAttempts(0);
            clientDao.save(client);
            log.info("Account is unlocked");
        }
    }

    private void authenticateClient(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
    }

    private void resetFailedAttempts(Client client) {
        client.setFailedAttempts(0);
        client.setAccountLocked(false);
        client.setLockTime(null);
        clientDao.save(client);
        log.info("Client {} authenticated successfully", client.getUsername());
    }

    private LoginResponse generateLoginResponse(Client client, HttpServletResponse response) {
        JWTResponse jwtToken = jwtService.generateToken(
                client.getId(),
                TOKEN_EXPIRE_TIME,
                TokenType.ACCESS_TOKEN,
                client.getUsername(),
                client.getName(),
                client.getRoles()
        );

        String refreshToken = jwtService.generateRefreshToken();

        refreshTokenLogic(client, refreshToken);


        CookieUtil.createCookie(response, "access_token", jwtToken.getToken(), true, (int) TOKEN_EXPIRE_TIME);
        CookieUtil.createCookie(response, "refresh_token", refreshToken, true, (int) REFRESH_TOKEN_EXPIRE_TIME);

        return new LoginResponse(jwtToken.getToken(), refreshToken, client.getUsername());
    }

    private void refreshTokenLogic(Client client, String refreshToken) {
        RefreshToken refreshTokenFound = refreshTokenDao.findByClient(client);

        Long id = Optional.ofNullable(refreshTokenFound)
                .map(RefreshToken::getId)
                .orElse(null);

        Integer trace = Optional.ofNullable(refreshTokenFound)
                .map(RefreshToken::getTrace)
                .map(t -> t + 1)
                .orElse(0);

        String updatedBy = Optional.ofNullable(refreshTokenFound)
                .map(RefreshToken::getCreatedBy)
                .orElse(null);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(id)
                .client(client)
                .active(true)
                .trace(trace)
                .token(refreshToken)
                .createdBy(client.getUsername())
                .status(TokenStatus.ACTIVE)
                .updatedBy(updatedBy)
                .expireDate(LocalDateTime.now().plusDays(30))
                .build();

        refreshTokenDao.save(newRefreshToken);
    }


    private void handleFailedLoginAttempt(Client client, String username) {
        int failedAttempts = client.getFailedAttempts() + 1;
        client.setFailedAttempts(failedAttempts);

        Optional.of(failedAttempts)
                .filter(attempts -> attempts >= MAX_ATTEMPTS)
                .ifPresent(attempts -> {
                    client.setAccountLocked(true);
                    if (client.getLockTime() == null) {
                        client.setLockTime(LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
                    }
                    log.warn("User {} has been locked due to {} failed attempts", username, attempts);
                    clientDao.save(client);
                    throw new GlobalException("Your account is locked due to too many failed attempts. Try again in " +
                            String.format("%02d:%02d", LOCK_TIME_DURATION, 0) + " minutes.");
                });

        clientDao.save(client);
        log.error("Invalid password for user: {}", username);
    }

    @Override
    public void requestPasswordReset(RequestResetPasswordRequest requestResetPasswordRequest) {
        Client clientFound = clientDao.findByUsername(requestResetPasswordRequest.getUsername())
                .orElseThrow(() -> new GlobalException("Client not found with username: " + requestResetPasswordRequest.getUsername()));

        JWTResponse token = jwtService.generateToken(clientFound.getId(), RESET_PASSWORD_TOKEN_TIME, TokenType.RESET_PASSWORD, clientFound.getUsername(), clientFound.getName(), clientFound.getRoles());
        PasswordResetToken passwordResetToken = new PasswordResetToken(null, token.getToken(), TokenStatus.ACTIVE, LocalDateTime.now().plusMinutes(10), clientFound);

        passwordResetTokenDao.save(passwordResetToken);
        emailService.sendRequestChangePassword(clientFound, "Password Reset Request", token.getToken());
    }
}
