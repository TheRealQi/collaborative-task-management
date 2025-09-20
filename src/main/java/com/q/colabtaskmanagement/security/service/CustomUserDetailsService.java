package com.q.colabtaskmanagement.security.service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.UUID;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(UUID id);
}
