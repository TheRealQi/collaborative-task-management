package com.q.colabtaskmanagement.dataaccess.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItem {
    private ObjectId id;
    private String title;
    private boolean completed;
    private Instant dueDate;
}