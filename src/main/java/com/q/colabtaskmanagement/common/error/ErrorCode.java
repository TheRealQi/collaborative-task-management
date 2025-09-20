package com.q.colabtaskmanagement.common.error;

public enum ErrorCode {
    BAD_REQUEST("BAD_REQUEST"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    UNAUTHORIZED("UNAUTHORIZED"),
    FORBIDDEN("FORBIDDEN"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    CONFLICT("CONFLICT"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    FILE_STORAGE_ERROR("FILE_STORAGE_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
