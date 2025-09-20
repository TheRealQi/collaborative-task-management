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

    @Override
    @Transactional
    public WorkspaceDTO editWorkspace(UUID workspaceId, WorkspaceEditDTO workspaceEditDTO, User_ user) {
        WorkspaceMember member = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, user.getId())).orElseThrow(() -> new ForbiddenException("You do not have access to this workspace."));
        if (member.getRole() != WorkspaceRole.ADMIN) {
            throw new ForbiddenException("Only admins can edit the workspace.");
        }
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        workspace.setTitle(workspaceEditDTO.getTitle());
        workspace.setDescription(workspaceEditDTO.getDescription());
        workspaceRepository.save(workspace);
        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), member.getRole());
    }


    @Override
    public void deleteWorkspaceById(UUID workspaceId) {

    }

    @Override
    public List<WorkspaceMembersDTO> getWorkspaceMembers(UUID workspaceId) {
        return List.of();
    }

    @Override
    public void removeMember(UUID workspaceID, UUID userId) {

    }

    @Override
    public void changeMemberRole(WorkspaceRoleChangeDTO workspaceRoleChangeDTO) {

    }

    @Override
    public WorkspaceRole getUserRoleInWorkspace(UUID workspaceId) {
        return null;
    }
}
