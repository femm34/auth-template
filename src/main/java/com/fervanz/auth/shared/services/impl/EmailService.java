package com.fervanz.auth.shared.services.impl;

import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.security.context.jwt.service.IJWTService;
import com.fervanz.auth.shared.services.IEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
public class EmailService implements IEmailService {
    public static final String FINANCIAL_SYSTEM_NOTIFICATIONS_MAIL = "femm15.mm@gmail.com";
    private final JavaMailSender mailSender;

    @Value("${app.ws.password-recovery-endpoint}")
    private String passwordRecoveryEndpoint;

    @Override
    public void sendRequestChangePassword(Client client, String subject, String token) {
        try {
            String body = readHtmlTemplate("templates/request-reset-password-template.html");
            body = replacePlaceholders(body, UUID.randomUUID().toString());
            body = replacePlaceholdersUrl(body, buildVerificationUri(token));

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(client.getEmail());
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(FINANCIAL_SYSTEM_NOTIFICATIONS_MAIL);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", client.getEmail(), e.getMessage());
        }

    }

    private String readHtmlTemplate(String path) throws IOException {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private String replacePlaceholders(String body, String code) {
        return body.replace("${code}", code);
    }
    private String replacePlaceholdersUrl(String body, String validationUrl) {
        return body.replace("${validationUrl}", validationUrl);
    }

    private String buildVerificationUri(String encodedData) {
        return passwordRecoveryEndpoint + "?q=" + encodedData;
    }
}
