package com.q.colabtaskmanagement.controller;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiSuccessResponse;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListEditDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListMoveDTO;
import com.q.colabtaskmanagement.service.interfaces.BoardContentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/lists")
public class BoardListController {
    private final BoardContentService boardContentService;

    public BoardListController(BoardContentService boardContentService) {
        this.boardContentService = boardContentService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BoardListDTO>> createBoardList(@PathVariable UUID boardId, @Valid @RequestBody BoardListCreationDTO boardListCreationDTO) {
        BoardListDTO createdList = boardContentService.createBoardList(boardId, boardListCreationDTO);
        ApiSuccessResponse<BoardListDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Board list created successfully", createdList, null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiSuccessResponse<List<BoardListDTO>>> getAllBoardLists(@PathVariable UUID boardId) {
        List<BoardListDTO> lists = boardContentService.getAllBoardLists(boardId);
        ApiSuccessResponse<List<BoardListDTO>> apiResponse =
                new ApiSuccessResponse<>(true, "Board lists fetched successfully", lists, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<ApiSuccessResponse<BoardListDTO>> updateBoardList(@PathVariable UUID boardId, @PathVariable String listId, @Valid @RequestBody BoardListEditDTO boardListEditDTO) {
        BoardListDTO updatedList = boardContentService.updateBoardList(boardId, listId, boardListEditDTO);
        ApiSuccessResponse<BoardListDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Board list updated successfully", updatedList, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteBoardList(@PathVariable UUID boardId, @PathVariable String listId) {
        boardContentService.deleteBoardList(boardId, listId);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Board list deleted successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{listId}/reorder")
    public ResponseEntity<ApiSuccessResponse<List<BoardListDTO>>> reorderBoardList(@PathVariable UUID boardId, @PathVariable String listId, @Valid @RequestBody BoardListMoveDTO boardListMoveDTO) {
        List<BoardListDTO> boardLists = boardContentService.moveBoardList(boardId, listId, boardListMoveDTO);
        ApiSuccessResponse<List<BoardListDTO>> apiResponse =
                new ApiSuccessResponse<>(true, "Board list reordered successfully", boardLists, null);
        return ResponseEntity.ok(apiResponse);
    }

}
