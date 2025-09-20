package com.q.colabtaskmanagement.common.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private String expiresIn;
    private String userId;
}
