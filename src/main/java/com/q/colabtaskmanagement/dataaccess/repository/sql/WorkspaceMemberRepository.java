package com.q.colabtaskmanagement.dataaccess.repository.sql;

import com.q.colabtaskmanagement.common.enums.WorkspaceRole;
import com.q.colabtaskmanagement.dataaccess.model.sql.User_;
import com.q.colabtaskmanagement.dataaccess.model.sql.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import com.q.colabtaskmanagement.dataaccess.repository.projection.WorkspaceBoardsProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {

    @Query("""
                SELECT b.id as boardId,
                       b.title as boardTitle,
                       bm.role as role,
                       b.visibility as boardVisibility,
                       w.id as workspaceId,
                       w.title as workspaceTitle,
                       wm.role as userWorkspaceRole
                FROM WorkspaceMember wm
                JOIN wm.workspace w
                LEFT JOIN w.boards b
                LEFT JOIN BoardMember bm ON bm.board.id = b.id AND bm.user.id = wm.user.id
                WHERE wm.user.id = :userId AND wm.role IN (com.q.colabtaskmanagement.common.enums.WorkspaceRole.ADMIN,
                                                                            com.q.colabtaskmanagement.common.enums.WorkspaceRole.MEMBER)
            """)
    List<WorkspaceBoardsProjection> findWorkspacesWithBoardsByUserId(@Param("userId") UUID userId);

    @Query("""
                SELECT b.id as boardId,
                       b.title as boardTitle,
                       bm.role as role,
                       b.visibility as boardVisibility,
                       w.id as workspaceId,
                       w.title as workspaceTitle,
                       wm.role as userWorkspaceRole
                FROM WorkspaceMember wm
                JOIN wm.workspace w
                LEFT JOIN w.boards b
                LEFT JOIN BoardMember bm ON bm.board.id = b.id AND bm.user.id = wm.user.id
                WHERE wm.user.id = :userId AND wm.role = com.q.colabtaskmanagement.common.enums.WorkspaceRole.GUEST
            """)
    List<WorkspaceBoardsProjection> findGuestWorkspacesWithBoardsByUserId(@Param("userId") UUID userId);

    Optional<WorkspaceMember> findByIdWorkspaceIdAndIdUserId(UUID workspaceId, UUID userId);

    List<WorkspaceMember> findByUser(User_ user);

    @EntityGraph(attributePaths = {"user"})
    List<WorkspaceMember> findByIdWorkspaceId(UUID workspaceId);

    long countByIdWorkspaceIdAndRole(UUID workspaceId, WorkspaceRole role);
}
