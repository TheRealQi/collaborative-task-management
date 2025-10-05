package com.q.colabtaskmanagement.common.dto.board.content.checklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDTO {
    private String id;
    private UUID boardId;
    private String title;
    private List<ChecklistItemDTO> items = List.of();
}
