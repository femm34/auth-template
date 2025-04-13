package com.fervanz.auth.security.models.entities;

import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.security.context.jwt.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "password_reset_token_entity")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime expireDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
