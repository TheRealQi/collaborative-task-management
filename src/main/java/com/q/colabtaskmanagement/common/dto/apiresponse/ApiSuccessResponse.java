package com.q.colabtaskmanagement.common.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApiSuccessResponse<T> {
    private boolean success = true;
    private String message;
    private T data;
    private Map<String, Object> metadata;
}
