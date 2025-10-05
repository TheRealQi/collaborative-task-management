package com.q.colabtaskmanagement.service.interfaces;

import com.q.colabtaskmanagement.common.dto.board.content.card.CardCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardMoveDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardUpdateDTO;
import com.q.colabtaskmanagement.common.dto.board.content.checklist.*;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentEditDTO;

import java.util.UUID;

public interface CardService {
    // Cards
    CardDTO getCardById(String cardId);

    CardDTO createCardInList(UUID boardId, String listId, CardCreationDTO cardCreationDTO);

    CardDTO updateCard(String cardId, CardUpdateDTO cardUpdateDTO);

    CardDTO moveCard(String cardId, CardMoveDTO cardMoveDTO);

    void deleteCard(String cardId);

    // Comments
    CommentDTO postComment(String cardId, CommentCreationDTO commentCreationDTO);

    CommentDTO editComment(String cardId, String commentId, CommentEditDTO commentEditDTO);

    void deleteComment(String cardId, String commentId);

    // Checklists
    ChecklistDTO createChecklist(String cardId, ChecklistCreationDTO checklistCreationDTO);

    ChecklistDTO editChecklist(String cardId, String checklistId, ChecklistEditDTO checklistEditDTO);

    void deleteChecklist(String cardId, String checklistId);

    ChecklistItemDTO addChecklistItem(String cardId, String checklistId, ChecklistItemCreationDTO checklistItemCreationDTO);

    ChecklistItemDTO toggleChecklistItem(String cardId, String checklistId, String checklistItemId, ChecklistItemToggleDTO checklistItemToggleDTO);

    void deleteChecklistItem(String cardId, String checklistId, String itemId);
}
