package com.q.colabtaskmanagement.service;

import com.q.colabtaskmanagement.common.dto.authentication.LoginRequestDTO;
import com.q.colabtaskmanagement.common.dto.authentication.LoginResponseDTO;
import com.q.colabtaskmanagement.common.dto.authentication.RegisterationRequestDTO;

public interface AuthenticationService {
    public LoginResponseDTO login(LoginRequestDTO loginRequest);
    public void register(RegisterationRequestDTO registerRequest);
}
