package com.q.colabtaskmanagement.exception;

public class JwtAuthenticationException extends RuntimeException {

    private static final String INVALID_JWT_MESSAGE = "Invalid JWT token";
    private static final String EXPIRED_JWT_MESSAGE = "Expired JWT token";

    public static class InvalidJwtTokenException extends JwtAuthenticationException {
        public InvalidJwtTokenException() {
            super(INVALID_JWT_MESSAGE);
        }
        public InvalidJwtTokenException(Throwable cause) {
            super(INVALID_JWT_MESSAGE, cause);
        }
    }

    public static class ExpiredJwtTokenException extends JwtAuthenticationException {
        public ExpiredJwtTokenException() {
            super(EXPIRED_JWT_MESSAGE);
        }
        public ExpiredJwtTokenException(Throwable cause) {
            super(EXPIRED_JWT_MESSAGE, cause);
        }
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

