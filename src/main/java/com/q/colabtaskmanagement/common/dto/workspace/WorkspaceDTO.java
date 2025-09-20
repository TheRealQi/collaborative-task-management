package com.q.colabtaskmanagement.common.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDTO {
    private UUID workspaceId;
    private String title;
    private String userRole;
}
