package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.dto.authentication.LoginRequestDTO;
import com.q.colabtaskmanagement.common.dto.authentication.LoginResponseDTO;
import com.q.colabtaskmanagement.common.dto.authentication.RegisterationRequestDTO;
import com.q.colabtaskmanagement.dataaccess.model.sql.User_;
import com.q.colabtaskmanagement.dataaccess.repository.sql.UserRepository;
import com.q.colabtaskmanagement.exception.FormConflictException;
import com.q.colabtaskmanagement.exception.UnauthorizedException;
import com.q.colabtaskmanagement.security.service.JwtFilterService;
import com.q.colabtaskmanagement.service.interfaces.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.q.colabtaskmanagement.common.error.ErrorMessages.EMAIL_ALREADY_EXISTS;
import static com.q.colabtaskmanagement.common.error.ErrorMessages.USERNAME_ALREADY_EXISTS;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtFilterService jwtService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtFilterService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        String password = loginRequest.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UUID userUUID = userRepository.findIdByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new UnauthorizedException("Invalid username/email or password"));
            String jwtToken = jwtService.generateToken(userUUID);
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setAccessToken(jwtToken);
            loginResponse.setExpiresIn(String.valueOf(jwtService.getExpirationInSeconds()));
            loginResponse.setUserId(userUUID.toString());

            return loginResponse;
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid username/email or password");
        }
    }

    @Override
    @Transactional
    public void register(RegisterationRequestDTO registerRequest) {
        String name = registerRequest.getName();
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        Map<String, String> errors = new HashMap<>();
        if (userRepository.existsByUsername(username)) {
            errors.put("username", USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            errors.put("email", EMAIL_ALREADY_EXISTS);
        }
        if (!errors.isEmpty()) {
            throw new FormConflictException(errors);
        }

        User_ newUser = new User_();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    }
}
