package com.q.colabtaskmanagement.common.dto.authentication;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterationRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can only contain letters, numbers, and underscores"
    )
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email Address is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
            message = "Password must contain at least one uppercase letter and one special character"
    )


    @Size(min = 12, message = "Password must be at least 12 characters long")
    @NotBlank(message = "Password is required")
    private String password;
}
