package com.q.colabtaskmanagement.dataaccess.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Document(collection = "board_cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    private ObjectId id;
    private UUID boardId;
    private ObjectId listId;
    private String title;
    private String description = "";
    private List<UUID> assignedMembers = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private List<Checklist> checklists = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private int position;
    private Instant dueDate = null;
    @CreatedDate
    private Instant createdAt = Instant.now();
    @LastModifiedDate
    private Instant updatedAt;
}