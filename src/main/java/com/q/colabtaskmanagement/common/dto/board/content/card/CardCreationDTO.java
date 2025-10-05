package com.q.colabtaskmanagement.common.dto.board.content.card;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreationDTO {
    @NotEmpty(message = "Title must not be empty")
    @Size(max = 256, message = "Title must not exceed 256 characters")
    private String title;
}
