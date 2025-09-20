package com.q.colabtaskmanagement.exception;

import java.util.Map;

public class FormConflictException extends ConflictException {
    private final Map<String, String> errors;

    public FormConflictException(Map<String, String> errors) {
        super("Form contains errors");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

