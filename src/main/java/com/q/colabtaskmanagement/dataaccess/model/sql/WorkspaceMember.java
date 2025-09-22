package com.q.colabtaskmanagement.dataaccess.model;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "workspace_member")
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WorkspaceMember {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private WorkspaceMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private WorkspaceRole role;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @MapsId("workspaceId")
    private Workspace workspace;

    @ManyToOne
    @MapsId("userId")
    private User_ user;

}
