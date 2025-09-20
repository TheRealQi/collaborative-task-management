package com.q.colabtaskmanagement.exception;

public class InternalServerErrorException extends AppException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
