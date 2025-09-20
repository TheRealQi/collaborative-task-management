package com.q.colabtaskmanagement.common.error;

/**
 * Centralized error messages for the application.
 * Use these constants in exceptions and API responses.
 */
public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already taken";

    public static final String INVALID_CREDENTIALS = "Invalid username/email or password";

    public static final String INVALID_JWT = "Invalid JWT token";
    public static final String EXPIRED_JWT = "JWT token has expired";

    public static final String ACCESS_DENIED = "Access denied";

    public static final String RESOURCE_NOT_FOUND = "The requested resource was not found";

    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";
}
