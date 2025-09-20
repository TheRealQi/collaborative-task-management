package com.q.colabtaskmanagement.common.dto.workspace;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMembersDTO {
    private UUID userId;
    private String name;
    private String username;
    private WorkspaceRole workspaceRole;
}
