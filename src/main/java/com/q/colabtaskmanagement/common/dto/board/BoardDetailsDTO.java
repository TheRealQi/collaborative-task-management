package com.q.colabtaskmanagement.common.dto.board;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BoardDetailsDTO {
    private UUID boardId;
    private String title;
    private String description;
    private BoardRole boardRole;
    private BoardVisibility visibility;
    private UUID workspaceId;
}
