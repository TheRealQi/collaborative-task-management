package com.q.colabtaskmanagement.common.dto.board.content.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String id;
    private String cardId;
    private String authorId;
    private String text;
    private Instant createdAt;
    private Instant updatedAt;
}
