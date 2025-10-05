package com.q.colabtaskmanagement.common.dto.board.content.checklist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
public class ChecklistItemCreationDTO {
    @NotEmpty(message = "Title must not be empty")
    @Size(max = 256, message = "Title must not exceed 256 characters")
    private String title;
    private Instant dueDate = null;
}
