package com.q.colabtaskmanagement.security.service;

import com.q.colabtaskmanagement.exception.UnauthorizedException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtFilterService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public long getExpirationInSeconds() {
        return expirationTime / 1000;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UUID uuid) {
        return Jwts.builder()
                .setSubject(uuid.toString())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Optional<UUID> validateAndExtractUUID(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable(UUID.fromString(claims.getSubject()));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("JWT token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }
}
