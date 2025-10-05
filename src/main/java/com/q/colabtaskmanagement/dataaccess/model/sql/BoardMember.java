package com.q.colabtaskmanagement.dataaccess.model.sql;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.dataaccess.model.id.BoardMemberId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Table(name = "board_member")
@Setter
@Getter
public class BoardMember {
    @EmbeddedId
    private BoardMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private BoardRole role;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private Instant joinedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @MapsId("boardId")
    private Board board;

    @ManyToOne
    @MapsId("userId")
    private User_ user;
}
