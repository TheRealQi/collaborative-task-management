package com.q.colabtaskmanagement.websocket;

import com.q.colabtaskmanagement.security.service.CustomUserDetailsService;
import com.q.colabtaskmanagement.security.service.JwtFilterService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtFilterService jwtFilterService;
    private final CustomUserDetailsService userDetailsService;

    public JwtWebSocketHandshakeInterceptor(JwtFilterService jwtFilterService, CustomUserDetailsService userDetailsService) {
        this.jwtFilterService = jwtFilterService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {
            String authHeader = servletServerHttpRequest.getServletRequest().getParameter("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                if (!jwt.isEmpty()) {
                    jwtFilterService.validateAndExtractUUID(jwt).ifPresent(uuid -> {
                        var userDetails = userDetailsService.loadUserById(uuid);
                        attributes.put("userDetails", userDetails);
                    });
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
