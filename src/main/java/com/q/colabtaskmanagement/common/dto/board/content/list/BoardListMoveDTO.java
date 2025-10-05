package com.q.colabtaskmanagement.common.dto.board.content.list;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BoardListMoveDTO {
    @PositiveOrZero(message = "Position must be zero or positive")
    private int targetPosition;
}

