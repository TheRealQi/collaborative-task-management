package com.q.colabtaskmanagement.common.dto.workspace;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRoleChangeDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "New role is required")
    private WorkspaceRole newRole;
}
