package com.q.colabtaskmanagement.common.dto.board.content;


import com.q.colabtaskmanagement.common.dto.board.content.list.BoardListDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardContentDTO {
    private String id;
    private UUID boardId;
    private List<BoardListDTO> lists;
}
