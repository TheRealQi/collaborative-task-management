package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.sql.Board;
import com.q.colabtaskmanagement.dataaccess.model.sql.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.repository.sql.BoardMemberRepository;
import com.q.colabtaskmanagement.dataaccess.repository.sql.BoardRepository;
import com.q.colabtaskmanagement.dataaccess.repository.sql.WorkspaceMemberRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public classPermissionService {

    private final BoardMemberRepository boardMemberRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final BoardRepository boardRepository;

    public PermissionService(BoardMemberRepository boardMemberRepository,
                             WorkspaceMemberRepository workspaceMemberRepository, BoardRepository boardRepository) {
        this.boardMemberRepository = boardMemberRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.boardRepository = boardRepository;
    }

    // Workspace Permissions
    public WorkspaceRole getWorkspaceRole(UUID workspaceId, UUID userId) {
        return workspaceMemberRepository.findByIdWorkspaceIdAndIdUserId(workspaceId, userId)
                .map(WorkspaceMember::getRole)
                .orElseThrow(() -> new ForbiddenException("You don't have access to this workspace"));
    }

    public boolean hasWorkspaceRoles(UUID workspaceId, UUID userId, WorkspaceRole... allowedRoles) {
        WorkspaceRole userWorkspaceRole = getWorkspaceRole(workspaceId, userId);
        for (WorkspaceRole allowedRole : allowedRoles) {
            if (allowedRole == userWorkspaceRole) {
                return true;
            }
        }
        return false;
    }

    public boolean isWorkspaceAdmin(UUID workspaceId, UUID userId) {
        return hasWorkspaceRoles(workspaceId, userId, WorkspaceRole.ADMIN);
    }

    // Check if user is either MEMBER or ADMIN of the workspace not guest
    public boolean isWorkspaceMember(UUID workspaceId, UUID userId) {
        return hasWorkspaceRoles(workspaceId, userId, WorkspaceRole.MEMBER, WorkspaceRole.ADMIN);
    }

    public boolean isNotLastWorkspaceAdmin(UUID workspaceId) {
        return workspaceMemberRepository.countByIdWorkspaceIdAndRole(workspaceId, WorkspaceRole.ADMIN) > 1;
    }

    // Board Permissions
    public BoardRole getBoardRole(UUID boardId, UUID userId) {
        return boardMemberRepository.findByIdBoardIdAndIdUserId(boardId, userId)
                .map(member -> member.getRole())
                .orElseThrow(() -> new ForbiddenException("You don't have access to this board"));
    }


    public boolean isBoardRoles(UUID boardId, UUID userId, BoardRole... allowedRoles) {
        BoardRole userBoardRole = getBoardRole(boardId, userId);
        for (BoardRole allowedRole : allowedRoles) {
            if (allowedRole == userBoardRole) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardAdmin(UUID boardId, UUID userId) {
        return isBoardRoles(boardId, userId, BoardRole.ADMIN);
    }

    public boolean isBoardMember(UUID boardId, UUID userId) {
        return isBoardRoles(boardId, userId, BoardRole.MEMBER, BoardRole.ADMIN, BoardRole.OBSERVER);
    }

    public boolean isNotLastBoardAdmin(UUID boardId) {
        return boardMemberRepository.countByIdBoardIdAndRole(boardId, BoardRole.ADMIN) > 1;
    }

    public boolean canViewBoard(UUID boardId, UUID userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ForbiddenException("Board not found"));
        if (board.getVisibility() == BoardVisibility.PUBLIC || board.getVisibility() == BoardVisibility.WORKSPACE) {
            return true;
        }
        return isBoardMember(boardId, userId);
    }

    public boolean canEditBoardContent(UUID boardId, UUID userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ForbiddenException("Board not found"));
        if (board.getVisibility() == BoardVisibility.PUBLIC) {
            return true;
        }
        return isBoardRoles(boardId, userId, BoardRole.MEMBER, BoardRole.ADMIN);
    }

}
