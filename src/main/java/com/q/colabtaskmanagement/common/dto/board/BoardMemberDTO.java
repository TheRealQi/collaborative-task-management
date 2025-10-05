package com.q.colabtaskmanagement.common.dto.board;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberDTO {
    private UUID userId;
    private String name;
    private String email;
    private BoardRole boardRole;
}
