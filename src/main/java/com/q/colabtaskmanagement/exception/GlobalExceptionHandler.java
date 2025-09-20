package com.q.colabtaskmanagement.exception;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiErrorResponse;
import com.q.colabtaskmanagement.common.dto.apiresponse.ApiFieldsErrorResponse;
import com.q.colabtaskmanagement.common.dto.apiresponse.FieldErrorDTO;
import com.q.colabtaskmanagement.common.error.ErrorCode;
import com.q.colabtaskmanagement.common.error.ErrorMessages;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.BAD_REQUEST.getCode(),
                ex.getMessage() != null ? ex.getMessage() : "Bad Request"
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiFieldsErrorResponse> handleValidation(Exception ex) {
        List<FieldErrorDTO> details;

        if (ex instanceof MethodArgumentNotValidException manve) {
            details = manve.getBindingResult().getFieldErrors().stream()
                    .map(err -> new FieldErrorDTO(err.getField(), err.getDefaultMessage()))
                    .collect(Collectors.toList());
        } else if (ex instanceof ValidationException ve) {
            details = List.of(new FieldErrorDTO("validation", ve.getMessage()));
        } else {
            details = List.of(new FieldErrorDTO("error", "Unknown validation error"));
        }

        ApiFieldsErrorResponse response = new ApiFieldsErrorResponse(
                "Validation failed",
                ErrorCode.VALIDATION_ERROR.getCode(),
                details
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiFieldsErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorDTO> details = ex.getConstraintViolations().stream()
                .map(v -> new FieldErrorDTO(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());

        ApiFieldsErrorResponse response = new ApiFieldsErrorResponse(
                "Validation failed",
                ErrorCode.VALIDATION_ERROR.getCode(),
                details
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FormConflictException.class)
    public ResponseEntity<ApiFieldsErrorResponse> handleFormException(FormConflictException ex) {
        List<FieldErrorDTO> details = ex.getErrors().entrySet().stream()
                .map(entry -> new FieldErrorDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        ApiFieldsErrorResponse response = new ApiFieldsErrorResponse(
                "Form submission error",
                ErrorCode.CONFLICT.getCode(),
                details
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.UNAUTHORIZED.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<ApiErrorResponse> handleForbidden(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.FORBIDDEN.getCode(),
                ex.getMessage() != null ? ex.getMessage() : "Access Denied"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.CONFLICT.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({InternalServerErrorException.class, Exception.class})
    public ResponseEntity<ApiErrorResponse> handleServerError(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                false,
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ex.getMessage() != null ? ex.getMessage() : ErrorMessages.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

