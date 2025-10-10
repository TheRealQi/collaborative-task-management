package com.q.colabtaskmanagement.mapper;

import com.q.colabtaskmanagement.common.dto.board.content.BoardContentDTO;
import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListDTO;
import com.q.colabtaskmanagement.dataaccess.model.mongodb.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardContentMapper {
    @Mapping(target = "id", expression = "java(boardContent.getId().toString())")
    BoardContentDTO toDTO(BoardContent boardContent);

    @Mapping(target = "id", expression = "java(list.getId().toString())")
    BoardListDTO toDTO(BoardList list);
}
