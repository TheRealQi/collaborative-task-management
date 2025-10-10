package com.q.colabtaskmanagement.mapper;

import com.q.colabtaskmanagement.common.dto.board.BoardMemberDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import com.q.colabtaskmanagement.common.dto.board.content.card.CardDetailsDTO;
import com.q.colabtaskmanagement.common.dto.board.content.checklist.ChecklistDTO;
import com.q.colabtaskmanagement.common.dto.board.content.checklist.ChecklistItemDTO;
import com.q.colabtaskmanagement.common.dto.board.content.comment.CommentDTO;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.Card;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.Checklist;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.ChecklistItem;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.Comment;
import com.q.colabtaskmanagement.dataaccess.model.sql.BoardMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "id", expression = "java(card.getId().toString())")
    @Mapping(target = "listId", expression = "java(card.getListId().toString())")
    CardDetailsDTO toDTO(Card card);

    @Mapping(target = "id", expression = "java(card.getId().toString())")
    @Mapping(target = "listId", expression = "java(card.getListId().toString())")
    CardDTO toCardDTO(Card card);

    @Mapping(target = "id", expression = "java(comment.getId().toString())")
    @Mapping(target = "cardId", expression = "java(comment.getCardId().toString())")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "id", expression = "java(checklist.getId().toString())")
    ChecklistDTO toDTO(Checklist checklist);

    @Mapping(target = "id", expression = "java(checklistItem.getId().toString())")
    ChecklistItemDTO toDTO(ChecklistItem checklistItem);
}
