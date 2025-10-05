package com.q.colabtaskmanagement.common.dto.apiresponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class ApiFieldsErrorResponse extends ApiErrorResponse {
    private List<FieldErrorDTO> errors;

    public ApiFieldsErrorResponse(String message, String errorCode, List<FieldErrorDTO> errors) {
        super(false, errorCode, message);
        this.errors = errors;
    }
}