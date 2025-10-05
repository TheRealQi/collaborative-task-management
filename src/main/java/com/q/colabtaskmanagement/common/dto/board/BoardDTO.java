package com.q.colabtaskmanagement.common.dto.board;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private UUID boardId;
    private String title;
    private BoardRole boardRole;
    private BoardVisibility visibility;
    private UUID workspaceId;
}
