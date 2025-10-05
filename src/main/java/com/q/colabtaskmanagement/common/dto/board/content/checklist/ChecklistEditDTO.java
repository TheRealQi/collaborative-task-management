package com.q.colabtaskmanagement.common.dto.board.content.checklist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChecklistEditDTO {
    @NotEmpty(message = "Checklist title must not be empty")
    @Size(max = 256, message = "Checklist title must not exceed 256 characters")
    private String title;
}
