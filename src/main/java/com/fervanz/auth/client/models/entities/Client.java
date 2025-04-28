package com.fervanz.auth.client.models.entities;

import com.fervanz.auth.security.context.UserPrincipal;
import com.fervanz.auth.security.models.entities.CustomRole;
import com.fervanz.auth.security.models.entities.PasswordResetToken;
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
import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "client_role",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<CustomRole> roles;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<PasswordResetToken> passwordResetTokens;

    public UserDetails toUserDetails() {
        return new UserPrincipal(this);
    }
}
