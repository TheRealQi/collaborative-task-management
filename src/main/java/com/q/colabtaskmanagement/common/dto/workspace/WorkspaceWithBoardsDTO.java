package com.q.colabtaskmanagement.common.dto.workspace;

import com.q.colabtaskmanagement.common.dto.board.BoardDTO;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceWithBoardsDTO {
    private UUID workspaceId;
    private String workspaceTitle;
    private WorkspaceRole workspaceRole;
    private List<BoardDTO> boards;
}