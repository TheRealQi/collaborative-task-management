package com.q.colabtaskmanagement.common.dto.board.content.checklist;

import com.sun.jdi.PrimitiveValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDTO {
    private String id;
    private String title;
    private boolean completed = false;
    private Instant dueDate = null;
}
