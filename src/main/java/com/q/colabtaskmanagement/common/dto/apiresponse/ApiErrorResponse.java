package com.q.colabtaskmanagement.common.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private boolean success = false;
    private String type;
    private String message;
}
