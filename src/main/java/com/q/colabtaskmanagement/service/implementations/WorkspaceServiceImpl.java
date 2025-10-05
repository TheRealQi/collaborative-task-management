package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.dto.board.BoardDTO;
import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.sql.User_;
import com.q.colabtaskmanagement.dataaccess.model.sql.Workspace;
import com.q.colabtaskmanagement.dataaccess.model.sql.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import com.q.colabtaskmanagement.dataaccess.repository.projection.WorkspaceBoardsProjection;
import com.q.colabtaskmanagement.dataaccess.repository.sql.UserRepository;
import com.q.colabtaskmanagement.dataaccess.repository.sql.WorkspaceMemberRepository;
import com.q.colabtaskmanagement.dataaccess.repository.sql.WorkspaceRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;
import com.q.colabtaskmanagement.security.SecurityUtils;
import com.q.colabtaskmanagement.service.interfaces.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
                                WorkspaceMemberRepository workspaceMemberRepository,
                                UserRepository userRepository,
                                PermissionService permissionService) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    @Override
    public Workspace getWorkspaceOrThrow(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
    }

    @Transactional
    @Override
    public WorkspaceDTO createWorkspace(WorkspaceCreationDTO workspaceCreationDTO) {
        User_ creator = SecurityUtils.getCurrentUser();

        Workspace workspace = new Workspace();
        workspace.setTitle(workspaceCreationDTO.getTitle());
        workspace.setDescription(workspaceCreationDTO.getDescription());
        workspaceRepository.save(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setId(new WorkspaceMemberId(workspace.getId(), creator.getId()));
        member.setUser(creator);
        member.setWorkspace(workspace);
        member.setRole(WorkspaceRole.ADMIN);

        workspaceMemberRepository.save(member);

        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), WorkspaceRole.ADMIN);
    }

    @Override
    public WorkspaceDTO getWorkspaceById(UUID workspaceId) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        WorkspaceRole role = workspaceMemberRepository
                .findById(new WorkspaceMemberId(workspaceId, currentUserId))
                .map(WorkspaceMember::getRole)
                .orElseThrow(() -> new ForbiddenException("You do not have access to this workspace."));
        return new WorkspaceDTO(workspace.getId(), workspace.getTitle(), workspace.getDescription(), role);
    }

    @Override
    public List<WorkspaceDTO> getAllUserWorkspaces() {
        User_ currentUser = SecurityUtils.getCurrentUser();
        return workspaceMemberRepository.findByUser(currentUser).stream()
                .map(member -> new WorkspaceDTO(
                        member.getWorkspace().getId(),
                        member.getWorkspace().getTitle(),
                        member.getWorkspace().getDescription(),
                        member.getRole()
                ))
                .toList();
    }

    @Override
    public List<WorkspaceWithBoardsDTO> getAllUserWorkspacesWithBoards() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<WorkspaceBoardsProjection> flatData = workspaceMemberRepository.findWorkspacesWithBoardsByUserId(currentUserId);
        return groupByWorkspace(flatData);
    }

    @Override
    public List<WorkspaceWithBoardsDTO> getAllGuestWorkspacesWithBoards() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<WorkspaceBoardsProjection> flatData = workspaceMemberRepository.findGuestWorkspacesWithBoardsByUserId(currentUserId);
        return groupByWorkspace(flatData);
    }

    private List<WorkspaceWithBoardsDTO> groupByWorkspace(List<WorkspaceBoardsProjection> flatData) {
        return flatData.stream()
                .collect(Collectors.groupingBy(
                        WorkspaceBoardsProjection::getWorkspaceId,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            WorkspaceBoardsProjection first = list.get(0);
                            List<BoardDTO> boards = list.stream()
                                    .map(b -> new BoardDTO(b.getBoardId(), b.getBoardTitle(), b.getRole(), b.getBoardVisibility(), b.getWorkspaceId()))
                                    .toList();
                            return new WorkspaceWithBoardsDTO(first.getWorkspaceId(), first.getWorkspaceTitle(), first.getWorkspaceRole(), boards);
                        })
                ))
                .values()
                .stream()
                .toList();
    }

    @Transactional
    @Override
    public void editWorkspace(UUID workspaceId, WorkspaceEditDTO workspaceEditDTO) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!permissionService.assertWorkspaceAdmin(workspaceId, currentUserId)) {
            throw new ForbiddenException("You do not have permission to edit this workspace.");
        }
        workspace.setTitle(workspaceEditDTO.getTitle());
        workspace.setDescription(workspaceEditDTO.getDescription());
        workspaceRepository.save(workspace);
    }

    @Override
    public void deleteWorkspaceById(UUID workspaceId) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!permissionService.assertWorkspaceAdmin(workspaceId, currentUserId)) {
            throw new ForbiddenException("You do not have permission to delete this workspace.");
        }
        workspaceRepository.delete(workspace);
    }

    @Override
    public List<WorkspaceMemberDTO> getWorkspaceMembers(UUID workspaceId) {
        getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!permissionService.isWorkspaceMember(workspaceId, currentUserId)) {
            throw new ForbiddenException("You do not have access to this workspace.");
        }
        List<WorkspaceMember> members = workspaceMemberRepository.findByIdWorkspaceId(workspaceId);
        return members.stream()
                .map(member -> new WorkspaceMemberDTO(
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getUsername(),
                        member.getRole()
                ))
                .toList();
    }

    @Override
    public void removeMember(UUID workspaceId, UUID userId) {
        getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        permissionService.assertWorkspaceAdmin(workspaceId, currentUserId);
        if (userId.equals(currentUserId)) {
            throw new ForbiddenException("Admins cannot remove themselves.");
        }

        WorkspaceMember member = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, userId))
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this workspace."));

        if (member.getRole() == WorkspaceRole.ADMIN && !permissionService.assertNotLastWorkspaceAdmin(workspaceId)) {
            throw new ForbiddenException("You cannot remove the last admin of the workspace.");
        }

        workspaceMemberRepository.delete(member);
    }

    @Override
    public void changeMemberRole(UUID workspaceId, WorkspaceRoleChangeDTO workspaceRoleChangeDTO) {
        getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        permissionService.assertWorkspaceAdmin(workspaceId, currentUserId);

        UUID userId = workspaceRoleChangeDTO.getUserId();
        if (userId.equals(currentUserId)) {
            throw new ForbiddenException("Admins cannot change their own role.");
        }

        WorkspaceMember member = workspaceMemberRepository.findByIdWorkspaceIdAndIdUserId(workspaceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this workspace."));

        if (member.getRole() == workspaceRoleChangeDTO.getNewRole()) {
            throw new IllegalArgumentException("User already has this role.");
        }

        if (member.getRole() == WorkspaceRole.ADMIN && !permissionService.assertNotLastWorkspaceAdmin(workspaceId)) {
            throw new ForbiddenException("You cannot change the last admin role.");
        }

        member.setRole(workspaceRoleChangeDTO.getNewRole());
        workspaceMemberRepository.save(member);
    }

    @Override
    public void leaveWorkspace(UUID workspaceId) {
        getWorkspaceOrThrow(workspaceId);
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        WorkspaceMember member = workspaceMemberRepository.findByIdWorkspaceIdAndIdUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this workspace."));

        if (member.getRole() == WorkspaceRole.ADMIN && !permissionService.assertNotLastWorkspaceAdmin(workspaceId)) {
            throw new ForbiddenException("You are the last admin of this workspace. Please assign another admin before leaving.");
        }

        workspaceMemberRepository.delete(member);
    }
}
