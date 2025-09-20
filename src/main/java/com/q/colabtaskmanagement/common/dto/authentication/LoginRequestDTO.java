package com.q.colabtaskmanagement.common.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;
    @NotBlank(message = "Password is required")
    private String password;
}
