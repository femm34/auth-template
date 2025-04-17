package com.fervanz.auth.client.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
public class ClientRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#_\\-])[A-Za-z\\d@$!%*?&#_\\-]{8,}$",
            message = "Password must be at least 8 characters long and include an uppercase letter, lowercase letter, number, and special character"
    )
    private String password;

    @NotBlank(message = "Password confirmation is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#_\\-])[A-Za-z\\d@$!%*?&#_\\-]{8,}$",
            message = "Password must be at least 8 characters long and include an uppercase letter, lowercase letter, number, and special character"
    )
    private String confirmPassword;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Mother surname is required")
    private String motherSurname;

    @NotBlank(message = "Father surname is required")
    private String fatherSurname;

    @NotEmpty(message = "At least one role must be provided")
    private Set<String> roles;
}
