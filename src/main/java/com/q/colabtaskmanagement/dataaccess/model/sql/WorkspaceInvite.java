package com.q.colabtaskmanagement.dataaccess.model.sql;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Data
public class WorkspaceInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;

    private UUID workspaceId;

    private UUID userId;

    @CreationTimestamp
    private Instant invitedAt = Instant.now();

    private Instant expiresAt;

    private boolean accepted = false;
    private boolean expired = false;

    @PrePersist
    public void prePersist() {
        if (expiresAt == null) {
            expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        }
    }
}