package com.q.colabtaskmanagement.common.dto.board.content.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CardMoveDTO {
    @NotNull(message = "Target list ID cannot be null")
    private String targetListId;

    @PositiveOrZero(message = "Position must be zero or positive")
    private int targetPosition;
}

