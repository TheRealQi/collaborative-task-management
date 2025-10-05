package com.q.colabtaskmanagement.common.dto.board.content.card;

import com.q.colabtaskmanagement.common.dto.board.content.checklist.ChecklistItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    String id;
    String listId;
    int position;
    String title;
    String description;
    List<UUID> assignedMembers = List.of();
    List<String> labels = List.of();
    List<ChecklistItemDTO> checklistItems = List.of();
    Instant dueDate = null;
}
