package com.q.colabtaskmanagement.common.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
    private Map<String, ApiError> errors;
}
