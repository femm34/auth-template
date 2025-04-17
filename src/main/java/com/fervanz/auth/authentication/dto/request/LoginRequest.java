package com.fervanz.auth.authentication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#_\\-])[A-Za-z\\d@$!%*?&#_\\-]{8,}$",
            message = "Password must be at least 8 characters long and include an uppercase letter, lowercase letter, number, and special character"
    )
    private String password;
}
