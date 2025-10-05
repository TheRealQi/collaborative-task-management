package com.q.colabtaskmanagement.controller;

import com.q.colabtaskmanagement.common.dto.apiresponse.ApiSuccessResponse;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardMoveDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardUpdateDTO;
import com.q.colabtaskmanagement.common.dto.board.content.checklist.*;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentEditDTO;
import com.q.colabtaskmanagement.service.interfaces.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/cards")
    public ResponseEntity<ApiSuccessResponse<CardDTO>> createCard(@PathVariable UUID boardId, @PathVariable String listId, @Valid @RequestBody CardCreationDTO cardCreationDTO) {
        CardDTO cardDTO = cardService.createCardInList(boardId, listId, cardCreationDTO);
        ApiSuccessResponse<CardDTO> response = new ApiSuccessResponse<>(true, "Card created successfully", cardDTO, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<ApiSuccessResponse<CardDTO>> getCardById(@PathVariable String cardId) {
        CardDTO cardDTO = cardService.getCardById(cardId);
        ApiSuccessResponse<CardDTO> response =
                new ApiSuccessResponse<>(true, "Card fetched successfully", cardDTO, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<ApiSuccessResponse<CardDTO>> updateCard(
            @PathVariable String cardId,
            @Valid @RequestBody CardUpdateDTO cardUpdateDTO) {
        CardDTO updatedCard = cardService.updateCard(cardId, cardUpdateDTO);
        ApiSuccessResponse<CardDTO> response =
                new ApiSuccessResponse<>(true, "Card updated successfully", updatedCard, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<ApiSuccessResponse<CardDTO>> moveCard(
            @PathVariable String cardId,
            @Valid @RequestBody CardMoveDTO cardMoveDTO) {
        CardDTO movedCard = cardService.moveCard(
                cardId,
                cardMoveDTO
        );
        ApiSuccessResponse<CardDTO> response =
                new ApiSuccessResponse<>(true, "Card moved successfully", movedCard, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteCard(@PathVariable String cardId) {
        cardService.deleteCard(cardId);
        ApiSuccessResponse<Void> response =
                new ApiSuccessResponse<>(true, "Card deleted successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<ApiSuccessResponse<CommentDTO>> createComment(
            @PathVariable String cardId,
            @Valid @RequestBody CommentCreationDTO commentCreationDTO) {
        CommentDTO comment = cardService.postComment(cardId, commentCreationDTO);
        ApiSuccessResponse<CommentDTO> response =
                new ApiSuccessResponse<>(true, "Comment posted successfully", comment, null);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<ApiSuccessResponse<CommentDTO>> editComment(
            @PathVariable String cardId,
            @PathVariable String commentId,
            @Valid @RequestBody CommentEditDTO commentEditDTO) {
        CommentDTO updatedComment = cardService.editComment(cardId, commentId, commentEditDTO);
        ApiSuccessResponse<CommentDTO> response =
                new ApiSuccessResponse<>(true, "Comment updated successfully", updatedComment, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteComment(
            @PathVariable String cardId,
            @PathVariable String commentId) {
        cardService.deleteComment(cardId, commentId);
        ApiSuccessResponse<Void> response =
                new ApiSuccessResponse<>(true, "Comment deleted successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cards/{cardId}/checklists")
    public ResponseEntity<ApiSuccessResponse<ChecklistDTO>> createChecklist(
            @PathVariable String cardId,
            @Valid @RequestBody ChecklistCreationDTO checklistCreationDTO) {
        ChecklistDTO checklist = cardService.createChecklist(cardId, checklistCreationDTO);
        ApiSuccessResponse<ChecklistDTO> response =
                new ApiSuccessResponse<>(true, "Checklist created successfully", checklist, null);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/cards/{cardId}/checklists/{checklistId}")
    public ResponseEntity<ApiSuccessResponse<ChecklistDTO>> editChecklist(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @Valid @RequestBody ChecklistEditDTO checklistEditDTO) {
        ChecklistDTO updatedChecklist = cardService.editChecklist(cardId, checklistId, checklistEditDTO);
        ApiSuccessResponse<ChecklistDTO> response =
                new ApiSuccessResponse<>(true, "Checklist updated successfully", updatedChecklist, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cards/{cardId}/checklists/{checklistId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteChecklist(
            @PathVariable String cardId,
            @PathVariable String checklistId) {
        cardService.deleteChecklist(cardId, checklistId);
        ApiSuccessResponse<Void> response =
                new ApiSuccessResponse<>(true, "Checklist deleted successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cards/{cardId}/checklists/{checklistId}/items")
    public ResponseEntity<ApiSuccessResponse<ChecklistItemDTO>> addChecklistItem(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @Valid @RequestBody ChecklistItemCreationDTO itemCreationDTO) {
        ChecklistItemDTO item = cardService.addChecklistItem(cardId, checklistId, itemCreationDTO);
        ApiSuccessResponse<ChecklistItemDTO> response =
                new ApiSuccessResponse<>(true, "Checklist item added successfully", item, null);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/cards/{cardId}/checklists/{checklistId}/items/{itemId}/toggle")
    public ResponseEntity<ApiSuccessResponse<ChecklistItemDTO>> toggleChecklistItem(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @PathVariable String itemId,
            @Valid @RequestBody ChecklistItemToggleDTO toggleDTO) {
        ChecklistItemDTO updatedItem = cardService.toggleChecklistItem(cardId, checklistId, itemId, toggleDTO);
        ApiSuccessResponse<ChecklistItemDTO> response =
                new ApiSuccessResponse<>(true, "Checklist item toggled successfully", updatedItem, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cards/{cardId}/checklists/{checklistId}/items/{itemId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteChecklistItem(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @PathVariable String itemId) {
        cardService.deleteChecklistItem(cardId, checklistId, itemId);
        ApiSuccessResponse<Void> response =
                new ApiSuccessResponse<>(true, "Checklist item deleted successfully", null, null);
        return ResponseEntity.ok(response);
    }

}
