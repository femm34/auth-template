package com.fervanz.auth.shared.services;

import com.fervanz.auth.client.models.entities.Client;
import org.springframework.scheduling.annotation.Async;

public interface IEmailService {
    @Async("taskExecutor")
    void sendRequestChangePassword(Client client, String subject, String token);
}