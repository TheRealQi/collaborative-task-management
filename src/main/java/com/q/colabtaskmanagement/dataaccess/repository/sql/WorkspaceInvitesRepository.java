package com.q.colabtaskmanagement.dataaccess.repository.sql;

import com.q.colabtaskmanagement.dataaccess.model.sql.WorkspaceInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceInvitesRepository extends JpaRepository<WorkspaceInvite, UUID> {
    boolean existsByWorkspaceIdAndUserIdAndAcceptedIsTrue(UUID workspaceId, UUID userId);

    Optional<WorkspaceInvite> findByWorkspaceIdAndUserIdAndAcceptedIsFalse(UUID workspaceId, UUID userId);
}
