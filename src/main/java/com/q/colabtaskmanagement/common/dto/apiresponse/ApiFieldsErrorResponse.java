package com.q.colabtaskmanagement.common.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ApiFormErrorResponse extends ApiErrorResponse {
    private List<FieldErrorDTO> errors;

    public ApiFormErrorResponse(HttpStatus status, String message, String errorCode, List<FieldErrorDTO> errors) {
        super(status, errorCode, message);
        this.errors = errors;
    }
}