package com.q.colabtaskmanagement.controller;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiSuccessResponse;
import com.q.colabtaskmanagement.common.dto.authentication.LoginRequestDTO;
import com.q.colabtaskmanagement.common.dto.authentication.LoginResponseDTO;
import com.q.colabtaskmanagement.common.dto.authentication.RegisterationRequestDTO;
import com.q.colabtaskmanagement.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authenticationService.login(loginRequest);

        ApiSuccessResponse<LoginResponseDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Login successful", response, null);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<Void>> register(@Valid @RequestBody RegisterationRequestDTO registerRequest) {
        authenticationService.register(registerRequest);

        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Registration successful", null, null);

        return ResponseEntity.ok(apiResponse);
    }
}