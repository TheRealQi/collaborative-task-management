public enum ErrorCode {
    // Authentication
    AUTH_FAILED("AUTH_FAILED"),
    JWT_INVALID("JWT_INVALID"),
    JWT_EXPIRED("JWT_EXPIRED"),

    // Validation
    VALIDATION_ERROR("VALIDATION_ERROR"),
    USERNAME_TAKEN("USERNAME_TAKEN"),
    EMAIL_INVALID("EMAIL_INVALID"),

    // Resources
    USER_NOT_FOUND("USER_NOT_FOUND"),
    TASK_NOT_FOUND("TASK_NOT_FOUND"),

    // Server
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}