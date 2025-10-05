package com.q.colabtaskmanagement.common.dto.board;

import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardEditDTO {
    @NotEmpty(message = "Board title cannot be empty")
    @Size(max = 100, message = "Board title cannot exceed 100 characters")
    private String title;
    private String description;
    @NotNull(message = "Board visibility cannot be empty")
    private BoardVisibility visibility;
}
