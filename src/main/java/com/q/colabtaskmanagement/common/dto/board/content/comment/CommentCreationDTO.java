package com.q.colabtaskmanagement.common.dto.board.content.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentCreationDTO {
    @NotEmpty
    @Size(max = 2000, message = "Comment text must not exceed 2000 characters")
    private String text;
}
