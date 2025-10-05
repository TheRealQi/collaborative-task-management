package com.q.colabtaskmanagement.dataaccess.model.sql;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Data
public class BoardInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID boardId;

    private UUID userId;

    @CreationTimestamp
    private Instant invitedAt = Instant.now();

    private Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

    private boolean accepted = false;
    private boolean expired = false;

    @PrePersist
    public void prePersist() {
        if (expiresAt == null) {
            expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        }
    }
}