package com.q.colabtaskmanagement.service;

import com.q.colabtaskmanagement.common.dto.authentication.LoginRequestDTO;
import com.q.colabtaskmanagement.common.dto.authentication.LoginResponseDTO;
import com.q.colabtaskmanagement.common.dto.authentication.RegisterationRequestDTO;
import com.q.colabtaskmanagement.dataaccess.model.User_;
import com.q.colabtaskmanagement.dataaccess.repository.UserRepository;
import com.q.colabtaskmanagement.security.service.JwtFilterService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UUID userUUID = userRepository.findUserByUsernameOrEmail(usernameOrEmail).map(User_::getId).orElseThrow(null); // TODO: Exception handling User not found
        String jwtToken = jwtService.generateToken(userUUID);
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setAccessToken(jwtToken);
        loginResponse.setExpiresIn(String.valueOf(jwtService.getExpirationInSeconds()));
        loginResponse.setUserId(userUUID.toString());

        return loginResponse;
    }

    @Override
    @Transactional
    public boolean register(RegisterationRequestDTO registerRequest) {
        String name = registerRequest.getName();
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken"); // TODO: Custom Exception
        } else if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already taken"); // TODO: Custom Exception
        }

        User_ newUser = new User_();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return true;
    }
}
