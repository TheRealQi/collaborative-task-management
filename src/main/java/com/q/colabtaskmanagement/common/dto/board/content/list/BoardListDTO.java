package com.q.colabtaskmanagement.common.dto.board.content.list;

import com.q.colabtaskmanagement.common.dto.board.content.card.CardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDTO {
    private String id;
    private String title;
    private int position;
    List<CardDTO> cards;
}
