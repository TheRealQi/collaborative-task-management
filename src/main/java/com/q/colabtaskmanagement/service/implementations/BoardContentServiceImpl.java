package com.q.colabtaskmanagement.service.implementations;

import com.q.colabtaskmanagement.common.dto.board.content.*;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListCreationDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListEditDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListMoveDTO;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.BoardContent;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.BoardList;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.Card;
import com.q.colabtaskmanagement.dataaccess.repository.mongodb.BoardContentRepository;
import com.q.colabtaskmanagement.dataaccess.repository.mongodb.CardRepository;
import com.q.colabtaskmanagement.exception.ForbiddenException;
import com.q.colabtaskmanagement.exception.ResourceNotFoundException;
import com.q.colabtaskmanagement.mapper.BoardContentMapper;
import com.q.colabtaskmanagement.mapper.CardMapper;
import com.q.colabtaskmanagement.security.SecurityUtils;
import com.q.colabtaskmanagement.service.interfaces.BoardContentService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardContentServiceImpl implements BoardContentService {
    private final BoardContentRepository boardContentRepository;
    private final CardRepository cardRepository;
    private final BoardContentMapper boardContentMapper;
    private final PermissionService permissionService;
    private final CardMapper cardMapper;

    public BoardContentServiceImpl(BoardContentRepository boardContentRepository, CardRepository cardRepository, BoardContentMapper boardContentMapper, PermissionService permissionService, CardMapper cardMapper) {
        this.boardContentRepository = boardContentRepository;
        this.cardRepository = cardRepository;
        this.boardContentMapper = boardContentMapper;
        this.permissionService = permissionService;
        this.cardMapper = cardMapper;
    }


    @Override
    public void createBoardContent(UUID boardId) {
        boardContentRepository.findByBoardId(boardId).ifPresent(e -> {
            throw new IllegalStateException("Board content already exists for boardId: " + boardId);
        });
        BoardContent boardContent = new BoardContent();
        boardContent.setBoardId(boardId);
        boardContent.setLists(new ArrayList<>());
        boardContentRepository.save(boardContent);
    }


    @Override
    public BoardContentDTO getBoardContentByBoardId(UUID boardId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canViewBoard(boardId, userId)) {
            throw new ForbiddenException("You do not have access to view this board.");
        }
        return boardContentRepository.findByBoardId(boardId).map(boardContentMapper::toDTO).orElseThrow(() -> new IllegalStateException("Board content not found for boardId: " + boardId));
    }

    @Override
    @Transactional("mongoTransactionManager")
    public void deleteBoardContent(UUID boardId) {
        cardRepository.deleteAllByBoardId(boardId);
        boardContentRepository.deleteByBoardId(boardId);
    }


    @Override
    public BoardListDTO createBoardList(UUID boardId, BoardListCreationDTO listCreationDTO) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(boardId, userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        BoardContent boardContent = boardContentRepository.findByBoardId(boardId).orElseThrow(() -> new ResourceNotFoundException("Board content not found for board"));
        BoardList newList = new BoardList(new ObjectId(), listCreationDTO.getTitle(), boardContent.getLists().size());
        boardContent.getLists().add(newList);
        boardContentRepository.save(boardContent);
        return boardContentMapper.toDTO(newList);
    }


    @Override
    public List<BoardListDTO> getAllBoardLists(UUID boardId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canViewBoard(boardId, userId)) {
            throw new ForbiddenException("You do not have access to view this board content.");
        }
        BoardContent boardContent = boardContentRepository.findByBoardId(boardId).orElseThrow(() -> new ResourceNotFoundException("Board content not found for board"));
        List<Card> allCards = cardRepository.findAllByBoardId(boardId);
        Map<String, List<CardDTO>> allCardsMap = allCards.stream().map(cardMapper::toDTO).collect(Collectors.groupingBy(CardDTO::getListId));
        return boardContent.getLists().stream().map(list -> new BoardListDTO(list.getId().toString(), list.getTitle(), list.getPosition(), allCardsMap.getOrDefault(list.getId().toString(), List.of()))).toList();
    }

    @Override
    public BoardListDTO updateBoardList(UUID boardId, String listId, BoardListEditDTO listEditDTO) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(boardId, userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        BoardContent boardContent = boardContentRepository.findByBoardId(boardId).orElseThrow(() -> new ResourceNotFoundException("Board content not found for board"));
        ObjectId objListId = new ObjectId(listId);
        BoardList listToUpdate = boardContent.getLists().stream().filter(list -> list.getId().equals(objListId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("List not found with id: " + listId));
        listToUpdate.setTitle(listEditDTO.getTitle());
        boardContentRepository.save(boardContent);
        return boardContentMapper.toDTO(listToUpdate);
    }

    @Override
    @Transactional("mongoTransactionManager")
    public void deleteBoardList(UUID boardId, String listId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(boardId, userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        BoardContent boardContent = boardContentRepository.findByBoardId(boardId).orElseThrow(() -> new ResourceNotFoundException("Board content not found for board"));
        ObjectId objListId = new ObjectId(listId);
        BoardList listToDelete = boardContent.getLists().stream().filter(list -> list.getId().equals(objListId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("List not found with id: " + listId));
        cardRepository.deleteAllByListId(new ObjectId(listId));
        boardContent.getLists().remove(listToDelete);
        int deletedPosition = listToDelete.getPosition();
        boardContent.getLists().stream().filter(list -> list.getPosition() > deletedPosition).forEach(list -> list.setPosition(list.getPosition() - 1));
        boardContentRepository.save(boardContent);
    }

    @Override
    public List<BoardListDTO> moveBoardList(UUID boardId, String listToMoveId, BoardListMoveDTO boardListMoveDTO) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (!permissionService.canEditBoardContent(boardId, userId)) {
            throw new ForbiddenException("You do not have permissions to modify this board content.");
        }
        BoardContent boardContent = boardContentRepository.findByBoardId(boardId).orElseThrow(() -> new ResourceNotFoundException("Board content not found for board"));
        ObjectId objListId = new ObjectId(listToMoveId);
        int targetPosition = boardListMoveDTO.getTargetPosition();
        BoardList listToMove = boardContent.getLists().stream().filter(list -> list.getId().equals(objListId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("List not found with id: " + listToMoveId));
        int currentPosition = listToMove.getPosition();
        if (targetPosition < 0) targetPosition = 0;
        if (targetPosition >= boardContent.getLists().size()) targetPosition = boardContent.getLists().size() - 1;
        if (currentPosition == targetPosition) return getAllBoardLists(boardId);
        final int finalTargetPosition = targetPosition;
        if (currentPosition < targetPosition) {
            boardContent.getLists().stream().filter(list -> list.getPosition() > currentPosition && list.getPosition() <= finalTargetPosition).forEach(list -> list.setPosition(list.getPosition() - 1));
        } else {
            boardContent.getLists().stream().filter(list -> list.getPosition() >= finalTargetPosition && list.getPosition() < currentPosition).forEach(list -> list.setPosition(list.getPosition() + 1));
        }
        listToMove.setPosition(targetPosition);
        boardContentRepository.save(boardContent);
        return getAllBoardLists(boardId);
    }
}
