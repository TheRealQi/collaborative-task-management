package com.q.colabtaskmanagement.dataaccess.model.id;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class WorkspaceMemberId implements Serializable {
    private UUID workspaceId;
    private UUID userId;
}
