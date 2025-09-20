package com.q.colabtaskmanagement.service;

import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.User_;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceDTO createWorkspace(WorkspaceCreationDTO workspaceCreationDTO, User_ creator);

    WorkspaceDTO getWorkspaceById(UUID workspaceId, User_ user);

    List<WorkspaceDTO> getAllUserWorkspaces(User_ user);

    WorkspaceDTO editWorkspace(UUID workspaceId, WorkspaceEditDTO workspaceEditDTO, User_ user);

    void deleteWorkspaceById(UUID workspaceId);

    // Members & Roles
    List<WorkspaceMembersDTO> getWorkspaceMembers(UUID workspaceId);

//    void inviteMember();

    void removeMember(UUID workspaceID, UUID userId);

    void changeMemberRole(WorkspaceRoleChangeDTO workspaceRoleChangeDTO);

    WorkspaceRole getUserRoleInWorkspace(UUID workspaceId);
}
