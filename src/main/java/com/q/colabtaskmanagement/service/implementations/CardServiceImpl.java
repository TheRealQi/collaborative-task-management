package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.dto.board.content.card.CardCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardMoveDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardUpdateDTO;
import com.q.colabtaskmanagement.common.dto.board.content.checklist.*;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentEditDTO;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.*;
import com.q.colabtaskmanagement.dataaccess.repository.mongodb.BoardContentRepository;
import com.q.colabtaskmanagement.dataaccess.repository.mongodb.CardRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;
import com.q.colabtaskmanagement.mapper.CardMapper;
import com.q.colabtaskmanagement.security.SecurityUtils;
import com.q.colabtaskmanagement.service.interfaces.CardService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class CardServiceImpl implements CardService {
    private final BoardContentRepository boardContentRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final PermissionService permissionService;

    public CardServiceImpl(BoardContentRepository boardContentRepository, CardRepository cardRepository, CardMapper cardMapper, PermissionService permissionService) {
        this.boardContentRepository = boardContentRepository;
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.permissionService = permissionService;
    }

    @Override
    public CardDTO createCardInList(UUID boardId, String listId, CardCreationDTO cardCreationDTO) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(boardId, userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        ObjectId objListId = new ObjectId(listId);
        int cardPosition = (int) cardRepository.countByListId(objListId);
        Card newCard = new Card();
        newCard.setId(new ObjectId());
        newCard.setBoardId(boardId);
        newCard.setListId(objListId);
        newCard.setTitle(cardCreationDTO.getTitle());
        newCard.setPosition(cardPosition);
        cardRepository.save(newCard);
        return cardMapper.toDTO(newCard);
    }

    @Override
    public CardDTO getCardById(String cardId) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canViewBoard(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have access this board content.");
        }
        return cardMapper.toDTO(card);
    }

    @Override
    public CardDTO updateCard(String cardId, CardUpdateDTO cardUpdateDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have access this board content.");
        }
        boolean changed = false;
        if (cardUpdateDTO.getTitle() != null && !cardUpdateDTO.getTitle().equals(card.getTitle())) {
            card.setTitle(cardUpdateDTO.getTitle());
            changed = true;
        }
        if (cardUpdateDTO.getDescription() != null && !cardUpdateDTO.getDescription().equals(card.getDescription())) {
            card.setDescription(cardUpdateDTO.getDescription());
            changed = true;
        }
        if (!changed) return cardMapper.toDTO(card);
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toDTO(updatedCard);
    }

    @Override
    @Transactional("mongoTransactionManager")
    public CardDTO moveCard(String cardId, CardMoveDTO cardMoveDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        ObjectId sourceListId = card.getListId();
        ObjectId targetListId = new ObjectId(cardMoveDTO.getTargetListId());
        int targetPosition = cardMoveDTO.getTargetPosition();
        BoardContent boardContent = boardContentRepository.findByBoardId(card.getBoardId()).orElseThrow(() -> new ResourceNotFoundException("Board content not found"));

        boolean targetListExists = boardContent.getLists().stream().anyMatch(list -> list.getId().equals(targetListId));
        if (!targetListExists) {
            throw new ResourceNotFoundException("Target list not found");
        }
        int maxTargetPosition = (int) cardRepository.countByListId(targetListId);
        if (sourceListId.equals(targetListId)) {
            maxTargetPosition--;
        }
        if (targetPosition < 0) targetPosition = 0;
        if (targetPosition > maxTargetPosition) targetPosition = maxTargetPosition;

        boolean isSameList = sourceListId.equals(targetListId);
        int currentPosition = card.getPosition();
        if (isSameList && currentPosition == targetPosition) {
            return cardMapper.toDTO(card);
        }
        if (isSameList) {
            if (currentPosition < targetPosition) {
                cardRepository.decrementPositionsBetween(sourceListId, currentPosition + 1, targetPosition);
            } else {
                cardRepository.incrementPositionsBetween(sourceListId, targetPosition, currentPosition - 1);
            }
        } else {
            cardRepository.decrementPositionsAfter(sourceListId, currentPosition);
            cardRepository.incrementPositionsFrom(targetListId, targetPosition);
            card.setListId(targetListId);
        }
        card.setPosition(targetPosition);
        cardRepository.save(card);
        return cardMapper.toDTO(card);
    }

    @Override
    @Transactional("mongoTransactionManager")
    public void deleteCard(String cardId) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        ObjectId listId = card.getListId();
        int cardPosition = card.getPosition();
        cardRepository.decrementPositionsAfter(listId, cardPosition);
        cardRepository.delete(card);
    }

    @Override
    public CommentDTO postComment(String cardId, CommentCreationDTO commentCreationDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have access to this board content.");
        }
        Comment comment = new Comment(new ObjectId(), new ObjectId(cardId), userId, commentCreationDTO.getText(), Instant.now(), null);
        card.getComments().add(comment);
        cardRepository.save(card);
        return cardMapper.toDTO(comment);
    }

    @Override
    public CommentDTO editComment(String cardId, String commentId, CommentEditDTO commentEditDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Comment comment = card.getComments().stream().filter(c -> c.getId().equals(new ObjectId(commentId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        if (!comment.getAuthorId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own comments.");
        }
        boolean changed = false;
        if (!commentEditDTO.getText().equals(comment.getText())) {
            comment.setText(commentEditDTO.getText());
            comment.setUpdatedAt(Instant.now());
            changed = true;
        }
        if (!changed) return cardMapper.toDTO(comment);
        cardRepository.save(card);
        return cardMapper.toDTO(comment);
    }

    @Override
    public void deleteComment(String cardId, String commentId) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Comment comment = card.getComments().stream().filter(c -> c.getId().equals(new ObjectId(commentId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        if (!comment.getAuthorId().equals(userId) && !permissionService.assertBoardAdmin(card.getBoardId(), userId)) {
            throw new ForbiddenException("You can't delete this comment.");
        }
        card.getComments().remove(comment);
        cardRepository.save(card);
    }

    @Override
    public ChecklistDTO createChecklist(String cardId, ChecklistCreationDTO checklistCreationDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Checklist newChecklist = new Checklist(new ObjectId(), checklistCreationDTO.getTitle(), new ArrayList<>(), null, Instant.now(), null);
        card.getChecklists().add(newChecklist);
        cardRepository.save(card);
        return cardMapper.toDTO(newChecklist);
    }

    @Override
    public ChecklistDTO editChecklist(String cardId, String checklistId, ChecklistEditDTO checklistEditDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Checklist checklist = card.getChecklists().stream().filter(cl -> cl.getId().equals(new ObjectId(checklistId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Checklist not found with id: " + checklistId));
        if (checklistEditDTO.getTitle().equals(checklist.getTitle())) {
            return cardMapper.toDTO(checklist);
        }
        checklist.setTitle(checklistEditDTO.getTitle());
        checklist.setUpdatedAt(Instant.now());
        cardRepository.save(card);
        return cardMapper.toDTO(checklist);
    }

    @Override
    public void deleteChecklist(String cardId, String checklistId) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        boolean removed = card.getChecklists().removeIf(cl -> cl.getId().equals(new ObjectId(checklistId)));
        if (!removed) throw new ResourceNotFoundException("Checklist not found with id: " + checklistId);
        cardRepository.save(card);
    }

    @Override
    public ChecklistItemDTO addChecklistItem(String cardId, String checklistId, ChecklistItemCreationDTO checklistItemCreationDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        ChecklistItem newItem = new ChecklistItem(new ObjectId(), checklistItemCreationDTO.getTitle(), false, checklistItemCreationDTO.getDueDate());
        Checklist checklist = card.getChecklists().stream().filter(cl -> cl.getId().equals(new ObjectId(checklistId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Checklist not found with id: " + checklistId));
        checklist.getItems().add(newItem);
        cardRepository.save(card);
        return cardMapper.toDTO(newItem);
    }

    @Override
    public ChecklistItemDTO toggleChecklistItem(String cardId, String checklistId, String checklistItemId, ChecklistItemToggleDTO checklistItemToggleDTO) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Checklist checklist = card.getChecklists().stream().filter(cl -> cl.getId().equals(new ObjectId(checklistId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Checklist not found with id: " + checklistId));
        ChecklistItem item = checklist.getItems().stream().filter(i -> i.getId().equals(new ObjectId(checklistItemId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with id: " + checklistItemId));
        item.setCompleted(checklistItemToggleDTO.isCompleted());
        cardRepository.save(card);
        return cardMapper.toDTO(item);
    }

    @Override
    public void deleteChecklistItem(String cardId, String checklistId, String itemId) {
        Card card = cardRepository.findById(new ObjectId(cardId)).orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(card.getBoardId(), userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        Checklist checklist = card.getChecklists().stream().filter(cl -> cl.getId().equals(new ObjectId(checklistId))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Checklist not found with id: " + checklistId));
        boolean removed = checklist.getItems().removeIf(i -> i.getId().equals(new ObjectId(itemId)));
        if (!removed) throw new ResourceNotFoundException("Checklist item not found with id: " + itemId);
        cardRepository.save(card);
    }
}
