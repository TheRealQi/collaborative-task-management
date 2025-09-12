package com.q.colabtaskmanagement.exception;

public final class ErrorMessages {
    private ErrorMessages() {
    }

    // Registration errors
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already taken";

    // Authentication errors
    public static final String INVALID_CREDENTIALS = "Invalid username or password";

    // JWT errors
    public static final String INVALID_JWT = "Invalid JWT token";
    public static final String EXPIRED_JWT = "Expired JWT token";
}
