package com.q.colabtaskmanagement.service.interfaces;

import com.q.colabtaskmanagement.common.dto.board.content.*;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListEditDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListMoveDTO;

import java.util.List;
import java.util.UUID;

public interface BoardContentService {

    // Board operations
    void createBoardContent(UUID boardId);

    BoardContentDTO getBoardContentByBoardId(UUID boardId);

    void deleteBoardContent(UUID boardId);

    // Lists
    BoardListDTO createBoardList(UUID boardId, BoardListCreationDTO listCreationDTO);

    List<BoardListDTO> getAllBoardLists(UUID boardId);

    BoardListDTO updateBoardList(UUID boardId, String listId, BoardListEditDTO listEditDTO);

    void deleteBoardList(UUID boardId, String listId);

    List<BoardListDTO> moveBoardList(UUID boardId, String listToMoveId, BoardListMoveDTO reorderDTO);
}
