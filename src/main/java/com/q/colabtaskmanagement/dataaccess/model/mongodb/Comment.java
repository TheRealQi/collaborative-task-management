package com.q.colabtaskmanagement.dataaccess.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private ObjectId id;
    private ObjectId cardId;
    private UUID authorId;
    private String text;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = null;
}
