package com.q.colabtaskmanagement.dataaccess.repository.projection;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;

import java.util.UUID;

public interface WorkspaceBoardsProjection {
    UUID getBoardId();

    String getBoardTitle();

    BoardVisibility getBoardVisibility();

    BoardRole getRole();

    UUID getWorkspaceId();

    String getWorkspaceTitle();

    WorkspaceRole getWorkspaceRole();
}
