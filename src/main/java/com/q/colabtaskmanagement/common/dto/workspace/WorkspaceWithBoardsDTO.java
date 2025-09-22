package com.q.colabtaskmanagement.common.dto.board;

import com.q.colabtaskmanagement.common.enums.BoardVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardWithWorkspaceDTO {
    private UUID boardId;
    private String boardTitle;
    private BoardVisibility boardVisibility;

    private UUID workspaceId;
    private String workspaceTitle;
}