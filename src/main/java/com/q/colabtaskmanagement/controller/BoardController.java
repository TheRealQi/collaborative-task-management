package com.q.colabtaskmanagement.controller;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiSuccessResponse;
import com.q.colabtaskmanagement.common.dto.board.*;
import com.q.colabtaskmanagement.common.dto.board.content.BoardContentDTO;
import com.q.colabtaskmanagement.service.interfaces.BoardContentService;
import com.q.colabtaskmanagement.service.interfaces.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final BoardContentService boardContentService;

    public BoardController(BoardService boardService, BoardContentService boardContentService) {
        this.boardService = boardService;
        this.boardContentService = boardContentService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BoardDetailsDTO>> createBoard(
            @Valid @RequestBody BoardCreationDTO boardCreationDTO) {
        BoardDetailsDTO boardDetailsDTO = boardService.createBoard(boardCreationDTO);
        ApiSuccessResponse<BoardDetailsDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Board created successfully", boardDetailsDTO, null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<BoardDetailsDTO>> getBoard(@PathVariable UUID boardId) {
        BoardDetailsDTO boardDetailsDTO = boardService.getBoardById(boardId);
        ApiSuccessResponse<BoardDetailsDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Board fetched successfully", boardDetailsDTO, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{boardId}/content")
    public ResponseEntity<ApiSuccessResponse<BoardContentDTO>> getBoardContent(@PathVariable UUID boardId) {
        BoardContentDTO boardContentDTO = boardContentService.getBoardContentByBoardId(boardId);
        ApiSuccessResponse<BoardContentDTO> apiResponse =
                new ApiSuccessResponse<>(true, "Board content fetched successfully", boardContentDTO, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<BoardDTO>>> getAllBoardsForUser() {
        List<BoardDTO> boards = boardService.getAllBoardsForUser();
        ApiSuccessResponse<List<BoardDTO>> apiResponse =
                new ApiSuccessResponse<>(true, "Boards fetched successfully", boards, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/ws/{workspaceId}")
    public ResponseEntity<ApiSuccessResponse<List<BoardDTO>>> getAllAccessibleBoardsInWorkspace(
            @PathVariable UUID workspaceId) {
        List<BoardDTO> boards = boardService.getAllAccessibleBoardsInWorkspace(workspaceId);
        ApiSuccessResponse<List<BoardDTO>> apiResponse =
                new ApiSuccessResponse<>(true, "Boards fetched successfully", boards, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<Void>> editBoard(
            @PathVariable UUID boardId,
            @Valid @RequestBody BoardEditDTO boardEditDTO) {
        boardService.editBoard(boardId, boardEditDTO);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Board updated successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteBoard(@PathVariable UUID boardId) {
        boardService.deleteBoardById(boardId);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Board deleted successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{boardId}/members")
    public ResponseEntity<ApiSuccessResponse<List<BoardMemberDTO>>> getBoardMembers(@PathVariable UUID boardId) {
        List<BoardMemberDTO> members = boardService.getBoardMembers(boardId);
        ApiSuccessResponse<List<BoardMemberDTO>> apiResponse =
                new ApiSuccessResponse<>(true, "Board members fetched successfully", members, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{boardId}/members/{userId}")
    public ResponseEntity<ApiSuccessResponse<Void>> removeBoardMember(
            @PathVariable UUID boardId,
            @PathVariable UUID userId) {
        boardService.removeMember(boardId, userId);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Member removed successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{boardId}/members/role")
    public ResponseEntity<ApiSuccessResponse<Void>> changeMemberRole(
            @PathVariable UUID boardId,
            @RequestBody BoardRoleChangeDTO roleChangeDTO) {
        boardService.changeMemberRole(boardId, roleChangeDTO);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Member role changed successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{boardId}/leave")
    public ResponseEntity<ApiSuccessResponse<Void>> leaveBoard(@PathVariable UUID boardId) {
        boardService.leaveBoard(boardId);
        ApiSuccessResponse<Void> apiResponse =
                new ApiSuccessResponse<>(true, "Left board successfully", null, null);
        return ResponseEntity.ok(apiResponse);
    }
}
