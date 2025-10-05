package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.dto.board.*;
import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.id.BoardMemberId;
import com.q.colabtaskmanagement.dataaccess.model.sql.*;
import com.q.colabtaskmanagement.dataaccess.repository.sql.BoardMemberRepository;
import com.q.colabtaskmanagement.dataaccess.repository.sql.BoardRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;
import com.q.colabtaskmanagement.security.SecurityUtils;
import com.q.colabtaskmanagement.service.interfaces.BoardContentService;
import com.q.colabtaskmanagement.service.interfaces.BoardService;
import com.q.colabtaskmanagement.service.interfaces.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMemberRepository boardMemberRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;
    private final BoardContentService boardContentService;
    private final PermissionService permissionService;

    public BoardServiceImpl(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository, WorkspaceService workspaceService, BoardContentService boardContentService, PermissionService permissionService) {
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
        this.workspaceService = workspaceService;
        this.boardContentService = boardContentService;
        this.permissionService = permissionService;
    }

    private UUID getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    private Board getBoardOrThrow(UUID boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    @Override
    @Transactional
    public BoardDetailsDTO createBoard(BoardCreationDTO boardCreationDTO) {
        UUID userId = getCurrentUserId();
        WorkspaceRole memberRole = permissionService.getWorkspaceRole(boardCreationDTO.getWorkspaceId(), userId);
        if (memberRole != WorkspaceRole.ADMIN && memberRole != WorkspaceRole.MEMBER) {
            throw new ForbiddenException("You do not have permission to create a board in this workspace.");
        }
        Board board = new Board();
        board.setTitle(boardCreationDTO.getTitle());
        board.setDescription(boardCreationDTO.getDescription());
        board.setVisibility(boardCreationDTO.getVisibility());
        board.setWorkspace(workspaceService.getWorkspaceOrThrow(boardCreationDTO.getWorkspaceId()));
        board = boardRepository.save(board);
        BoardMember boardMember = new BoardMember();
        boardMember.setId(new BoardMemberId(board.getId(), userId));
        boardMember.setBoard(board);
        boardMember.setRole(BoardRole.ADMIN);
        boardMemberRepository.save(boardMember);
        try {
            boardContentService.createBoardContent(board.getId());
        } catch (Exception e) {
            boardMemberRepository.deleteById(boardMember.getId());
            boardRepository.delete(board);
            throw new RuntimeException("Failed to create board content", e);
        }
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        boardDetailsDTO.setBoardId(board.getId());
        boardDetailsDTO.setTitle(board.getTitle());
        boardDetailsDTO.setDescription(board.getDescription());
        boardDetailsDTO.setBoardRole(BoardRole.ADMIN);
        boardDetailsDTO.setVisibility(board.getVisibility());
        boardDetailsDTO.setWorkspaceId(board.getWorkspace().getId());
        return boardDetailsDTO;
    }

    @Override
    public void deleteBoardById(UUID boardId) {
        UUID userId = getCurrentUserId();
        if (!permissionService.assertBoardAdmin(boardId, userId)) {
            throw new ForbiddenException("Only admins can delete the board.");
        }
        Board board = getBoardOrThrow(boardId);
        try {
            boardContentService.deleteBoardContent(boardId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete board content", e);
        }
        deleteBoardAndMembers(boardId, board);
    }

    @Transactional
    public void deleteBoardAndMembers(UUID boardId, Board board) {
        boardMemberRepository.deleteAllByIdBoardId(boardId);
        boardRepository.delete(board);
    }

    @Override
    public BoardDetailsDTO getBoardById(UUID boardId) {
        UUID userId = getCurrentUserId();
        Board board = getBoardOrThrow(boardId);

        if (!permissionService.canViewBoard(boardId, userId)) {
            throw new ForbiddenException("You do not have access to view this board.");
        }

        BoardRole boardRole = boardMemberRepository.findById(new BoardMemberId(boardId, userId)).map(BoardMember::getRole).orElse(null); // null if observer or public

        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        boardDetailsDTO.setBoardId(board.getId());
        boardDetailsDTO.setTitle(board.getTitle());
        boardDetailsDTO.setDescription(board.getDescription());
        boardDetailsDTO.setBoardRole(boardRole);
        boardDetailsDTO.setVisibility(board.getVisibility());
        boardDetailsDTO.setWorkspaceId(board.getWorkspace().getId());
        return boardDetailsDTO;
    }

    @Override
    public List<BoardDTO> getAllBoardsForUser() {
        UUID userId = getCurrentUserId();
        return boardRepository.findBoardsByUserId(userId);
    }

    @Override
    public List<BoardDTO> getAllAccessibleBoardsInWorkspace(UUID workspaceId) {
        UUID userId = getCurrentUserId();
        workspaceService.getWorkspaceOrThrow(workspaceId);

        WorkspaceRole workspaceRole = permissionService.getWorkspaceRole(workspaceId, userId);
        List<BoardDTO> allBoards = boardRepository.findAllBoardsByWorkspaceId(workspaceId);

        return allBoards.stream().filter(board -> {
            switch (workspaceRole) {
                case ADMIN:
                    return true;
                case MEMBER:
                    return permissionService.isBoardMember(board.getBoardId(), userId) || board.getVisibility() == BoardVisibility.WORKSPACE || board.getVisibility() == BoardVisibility.PUBLIC;
                case GUEST:
                    return permissionService.isBoardMember(board.getBoardId(), userId) || board.getVisibility() == BoardVisibility.PUBLIC;
                default:
                    return false;
            }
        }).toList();
    }

    @Override
    public void editBoard(UUID boardId, BoardEditDTO boardUpdateDTO) {
        UUID userId = getCurrentUserId();
        if (!permissionService.assertBoardAdmin(boardId, userId)) {
            throw new ForbiddenException("Only admins can edit the board.");
        }

        Board board = getBoardOrThrow(boardId);
        board.setTitle(boardUpdateDTO.getTitle());
        board.setDescription(boardUpdateDTO.getDescription());
        board.setVisibility(boardUpdateDTO.getVisibility());
        boardRepository.save(board);
    }

    @Override
    public List<BoardMemberDTO> getBoardMembers(UUID boardId) {
        UUID userId = getCurrentUserId();
        if (!permissionService.isBoardMember(boardId, userId)) {
            throw new ForbiddenException("You do not have access to this board.");
        }

        List<BoardMember> members = boardMemberRepository.findByIdBoardId(boardId);
        return members.stream().map(m -> new BoardMemberDTO(m.getId().getUserId(), m.getUser().getName(), m.getUser().getEmail(), m.getRole())).toList();
    }

    @Override
    public void removeMember(UUID boardId, UUID targetUserId) {
        UUID userId = getCurrentUserId();
        if (!permissionService.assertBoardAdmin(boardId, userId)) {
            throw new ForbiddenException("Only admins can remove members.");
        }
        BoardMember boardMember = boardMemberRepository.findById(new BoardMemberId(boardId, targetUserId)).orElseThrow(() -> new ResourceNotFoundException("Member not found in this board."));

        if (boardMember.getRole() == BoardRole.ADMIN && !permissionService.assertNotLastBoardAdmin(boardId)) {
            throw new ForbiddenException("Cannot remove the last admin.");
        }
        boardMemberRepository.delete(boardMember);
    }

    @Override
    public void leaveBoard(UUID boardId) {
        UUID userId = getCurrentUserId();
        BoardMember boardMember = boardMemberRepository.findById(new BoardMemberId(boardId, userId)).orElseThrow(() -> new ForbiddenException("You do not have access to this board."));

        if (boardMember.getRole() == BoardRole.ADMIN && !permissionService.assertNotLastBoardAdmin(boardId)) {
            throw new ForbiddenException("You are the last admin. Assign another admin before leaving.");
        }

        boardMemberRepository.delete(boardMember);
    }

    @Override
    @Transactional
    public void changeMemberRole(UUID boardId, BoardRoleChangeDTO boardRoleChangeDTO) {
        UUID userId = getCurrentUserId();
        if (!permissionService.assertBoardAdmin(boardId, userId)) {
            throw new ForbiddenException("Only admins can change member roles.");
        }

        BoardMember memberToChange = boardMemberRepository.findById(new BoardMemberId(boardId, boardRoleChangeDTO.getUserId())).orElseThrow(() -> new ResourceNotFoundException("Member not found."));

        if (memberToChange.getRole() == boardRoleChangeDTO.getNewRole()) {
            throw new IllegalArgumentException("User already has this role.");
        }

        if (memberToChange.getRole() == BoardRole.ADMIN && !permissionService.assertNotLastBoardAdmin(boardId)) {
            throw new ForbiddenException("Cannot change the last admin role.");
        }

        memberToChange.setRole(boardRoleChangeDTO.getNewRole());
        boardMemberRepository.save(memberToChange);
    }
}
