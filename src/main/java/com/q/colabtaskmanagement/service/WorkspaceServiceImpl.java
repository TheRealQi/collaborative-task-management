package com.q.colabtaskmanagement.service;

import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.User_;
import com.q.colabtaskmanagement.dataaccess.model.Workspace;
import com.q.colabtaskmanagement.dataaccess.model.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import com.q.colabtaskmanagement.dataaccess.repository.WorkspaceMemberRepository;
import com.q.colabtaskmanagement.dataaccess.repository.WorkspaceRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    @Transactional
    @Override
    public WorkspaceDTO createWorkspace(WorkspaceCreationDTO workspaceCreationDTO, User_ creator) {

        String title = workspaceCreationDTO.getTitle();
        String description = workspaceCreationDTO.getDescription();

        Workspace workspace = new Workspace();
        workspace.setTitle(title);
        workspace.setDescription(description);

        WorkspaceMember workspaceMember = new WorkspaceMember();
        workspaceMember.setId(new WorkspaceMemberId(creator.getId(), workspace.getId()));
        workspaceMember.setUser(creator);
        workspaceMember.setWorkspace(workspace);
        workspaceMember.setRole(WorkspaceRole.ADMIN);

        workspaceRepository.save(workspace);
        workspaceMemberRepository.save(workspaceMember);

        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), WorkspaceRole.ADMIN);
    }


    @Override
    public WorkspaceDTO getWorkspaceById(UUID workspaceId, User_ user) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        WorkspaceRole userRole = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, user.getId())).map(WorkspaceMember::getRole).orElseThrow(() -> new ForbiddenException("You do not have access to this workspace."));
        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), userRole);
    }

    @Override
    public List<WorkspaceDTO> getAllUserWorkspaces(@AuthenticationPrincipal User_ user) {
        return workspaceMemberRepository.findByUser(user).stream()
                .map(member -> new WorkspaceDTO(
                        member.getWorkspace().getId(),
                        member.getWorkspace().getTitle(),
                        member.getWorkspace().getDescription(),
                        member.getRole()
                ))
                .toList();
    }

    private record RoleCheckResult(boolean isAdmin, WorkspaceRole role) {
    }

    private RoleCheckResult checkUserRole(UUID workspaceId, UUID userId, WorkspaceRole... allowedRoles) {
        return workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, userId))
                .map(member -> {
                    boolean hasAccess = Arrays.asList(allowedRoles).contains(member.getRole());
                    return new RoleCheckResult(hasAccess, member.getRole());
                })
                .orElse(new RoleCheckResult(false, null));
    }

    @Override
    @Transactional
    public WorkspaceDTO editWorkspace(UUID workspaceId, WorkspaceEditDTO workspaceEditDTO, User_ user) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        RoleCheckResult result = checkUserRole(workspaceId, user.getId(), WorkspaceRole.ADMIN);
        if (!result.isAdmin) {
            throw new ForbiddenException("Only admins can edit the workspace.");
        }
        workspace.setTitle(workspaceEditDTO.getTitle());
        workspace.setDescription(workspaceEditDTO.getDescription());
        workspaceRepository.save(workspace);
        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), result.role);
    }

    @Override
    public void deleteWorkspaceById(UUID workspaceId, User_ user) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        RoleCheckResult result = checkUserRole(workspaceId, user.getId(), WorkspaceRole.ADMIN);
        if (!result.isAdmin) {
            throw new ForbiddenException("Only admins can delete the workspace.");
        }
        workspaceRepository.delete(workspace);
    }


    @Override
    public List<WorkspaceMembersDTO> getWorkspaceMembers(UUID workspaceId, User_ user) {
        boolean exists = workspaceRepository.existsById(workspaceId);
        if (!exists) {
            throw new ResourceNotFoundException("Workspace not found");
        }
        RoleCheckResult result = checkUserRole(workspaceId, user.getId(), WorkspaceRole.ADMIN, WorkspaceRole.MEMBER);
        if (result.role == null) {
            throw new ForbiddenException("You do not have access to this workspace.");
        }
        if (result.role != WorkspaceRole.ADMIN && result.role != WorkspaceRole.MEMBER) {
            throw new ForbiddenException("You do not have access to view members of this workspace.");
        }
        List<WorkspaceMember> members = workspaceMemberRepository.findByIdWorkspaceId(workspaceId);

        return members.stream()
                .map(member -> new WorkspaceMembersDTO(
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getUsername(),
                        member.getRole()
                ))
                .toList();
    }

    @Override
    public void removeMember(UUID workspaceID, UUID userId, User_ user) {
        boolean exists = workspaceRepository.existsById(workspaceID);
        if (!exists) {
            throw new ResourceNotFoundException("Workspace not found");
        }
        RoleCheckResult result = checkUserRole(workspaceID, user.getId(), WorkspaceRole.ADMIN);
        if (!result.isAdmin) {
            throw new ForbiddenException("You do not have permission to remove members.");
        }
        if (userId.equals(user.getId())) {
            throw new ForbiddenException("Admins cannot remove themselves.");
        }
        WorkspaceMemberId memberId = new WorkspaceMemberId(userId, workspaceID);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member not found in this workspace."));
        workspaceMemberRepository.delete(member);
    }

    @Override
    public void changeMemberRole(UUID workspaceId, WorkspaceRoleChangeDTO workspaceRoleChangeDTO, User_ user) {
        boolean exists = workspaceRepository.existsById(workspaceId);
        if (!exists) {
            throw new ResourceNotFoundException("Workspace not found");
        }
        RoleCheckResult result = checkUserRole(workspaceId, user.getId(), WorkspaceRole.ADMIN);
        if (!result.isAdmin) {
            throw new ForbiddenException("You do not have permission to change members roles.");
        }
        UUID userId = workspaceRoleChangeDTO.getUserId();
        if (userId.equals(user.getId())) {
            throw new ForbiddenException("Admins cannot change their own role.");
        }
        WorkspaceMemberId memberId = new WorkspaceMemberId(userId, workspaceId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member not found in this workspace."));
        member.setRole(workspaceRoleChangeDTO.getNewRole());
        workspaceMemberRepository.save(member);
    }

    @Override
    public void leaveWorkspace(UUID workspaceId, User_ user) {
        boolean exists = workspaceRepository.existsById(workspaceId);
        if (!exists) {
            throw new ResourceNotFoundException("Workspace not found");
        }
        RoleCheckResult result = checkUserRole(workspaceId, user.getId(), WorkspaceRole.ADMIN, WorkspaceRole.MEMBER);
        if (result.role == null) {
            throw new ForbiddenException("You do not have access to this workspace.");
        }
        if (result.isAdmin) {
            long adminCount = workspaceMemberRepository.countByIdWorkspaceIdAndRole(workspaceId, WorkspaceRole.ADMIN);
            if (adminCount <= 1) {
                throw new ForbiddenException("You are the last admin.");
            }
        }
        WorkspaceMemberId memberId = new WorkspaceMemberId(user.getId(), workspaceId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("You are not a member of this workspace."));
        workspaceMemberRepository.delete(member);
    }


}
