package com.q.colabtaskmanagement.dataaccess.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checklist {
    private ObjectId id = new ObjectId();
    private String title = "";
    private List<ChecklistItem> items = new ArrayList<>();
    private Instant dueDate = null;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = null;
}
