package com.q.colabtaskmanagement.common.dto.apiresponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorDTO {
    private String field;
    private String message;
}
