package com.fervanz.auth.client.models.entities;

import com.fervanz.auth.security.context.UserPrincipal;
import com.fervanz.auth.shared.models.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "client_entity")
public class Client extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "scope")
    private String scope;

    @Column(name = "name")
    private String name;

    @Column(name = "mother_surname")
    private String motherSurname;

    @Column(name = "father_surname")
    private String fatherSurname;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;

    @Column(name = "account_locked")
    private boolean accountLocked = false;

    public UserDetails toUserDetails() {
        return new UserPrincipal(this);
    }
}
