package com.q.colabtaskmanagement.service.interfaces;

import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.dataaccess.model.sql.User_;
import com.q.colabtaskmanagement.dataaccess.model.sql.Workspace;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceDTO createWorkspace(WorkspaceCreationDTO workspaceCreationDTO);

    Workspace getWorkspaceOrThrow(UUID workspaceId);

    WorkspaceDTO getWorkspaceById(UUID workspaceId);

    List<WorkspaceDTO> getAllUserWorkspaces();

    List<WorkspaceWithBoardsDTO> getAllUserWorkspacesWithBoards();

    List<WorkspaceWithBoardsDTO> getAllGuestWorkspacesWithBoards();

    void editWorkspace(UUID workspaceId, WorkspaceEditDTO workspaceEditDTO);

    void deleteWorkspaceById(UUID workspaceId);

    List<WorkspaceMemberDTO> getWorkspaceMembers(UUID workspaceId);

    void removeMember(UUID workspaceID, UUID userId);

    void changeMemberRole(UUID workspaceId, WorkspaceRoleChangeDTO workspaceRoleChangeDTO);

    void leaveWorkspace(UUID workspaceId);
}

