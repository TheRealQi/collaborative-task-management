package com.q.colabtaskmanagement.common.dto.apiresponse;
import com.q.colabtaskmanagement.common.dto.apiresponse.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormErrorResponse {
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldErrorDTO> details;
}