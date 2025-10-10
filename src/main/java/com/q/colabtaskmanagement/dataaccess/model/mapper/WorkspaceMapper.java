
package com.q.colabtaskmanagement.mapper;

import com.q.colabtaskmanagement.common.dto.workspace.*;
import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.sql.Workspace;
import com.q.colabtaskmanagement.dataaccess.model.sql.WorkspaceMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    WorkspaceDTO toWorkspaceDTO(Workspace workspace, WorkspaceRole role);

    WorkspaceMemberDTO toWorkspaceMemberDTO(WorkspaceMember workspaceMember);
}