package com.fervanz.auth.authentication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
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
}
