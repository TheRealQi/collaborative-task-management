package com.q.colabtaskmanagement.service.interfaces;

import com.q.colabtaskmanagement.common.dto.board.*;

import java.util.List;
import java.util.UUID;

public interface BoardService {
    BoardDetailsDTO createBoard(BoardCreationDTO boardCreationDTO);

    BoardDetailsDTO getBoardById(UUID boardId);

    List<BoardDTO> getAllBoardsForUser();

    List<BoardDTO> getAllAccessibleBoardsInWorkspace(UUID workspaceId);

    void editBoard(UUID boardId, BoardEditDTO boardUpdateDTO);

    void deleteBoardById(UUID boardId);

    List<BoardMemberDTO> getBoardMembers(UUID boardId);

    void removeMember(UUID boardId, UUID userId);

    void leaveBoard(UUID boardId);

    void changeMemberRole(UUID boardId, BoardRoleChangeDTO boardRoleChangeDTO);
}
