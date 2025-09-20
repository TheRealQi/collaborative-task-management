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

    void deleteWorkspaceById(UUID workspaceId, User_ user);

    // Members & Roles
    List<WorkspaceMembersDTO> getWorkspaceMembers(UUID workspaceId, User_ user);

//    void inviteMember();

    void removeMember(UUID workspaceID, UUID userId, User_ user);

    void changeMemberRole(UUID workspaceId, WorkspaceRoleChangeDTO workspaceRoleChangeDTO, User_ user);

    void leaveWorkspace(UUID workspaceId, User_ user);
}
