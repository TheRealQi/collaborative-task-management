package com.q.colabtaskmanagement.dataaccess.repository;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.User_;
import com.q.colabtaskmanagement.dataaccess.model.Workspace;
import com.q.colabtaskmanagement.dataaccess.model.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {

    boolean existsByIdUserIdAndWorkspaceIdAndRoleIn(UUID userId, UUID workspaceId, List<WorkspaceRole> roles);

    List<WorkspaceMember> findByUser(User_ user);

    @EntityGraph(attributePaths = {"user"})
    List<WorkspaceMember> findByIdWorkspaceId(UUID workspaceId);

    long countByIdWorkspaceIdAndRole(UUID workspaceId, WorkspaceRole role);
}
