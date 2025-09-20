package com.q.colabtaskmanagement.dataaccess.model;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class WorkspaceInvites {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;

    private UUID workspaceId;

    private UUID userId;

    @CreationTimestamp
    private LocalDateTime invitedAt = LocalDateTime.now();
    private LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

    private boolean accepted = false;
    private boolean expired = false;

    @PrePersist
    public void prePersist() {
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(7);
        }
    }
}