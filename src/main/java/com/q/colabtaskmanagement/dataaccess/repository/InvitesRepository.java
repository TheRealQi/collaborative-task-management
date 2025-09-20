package com.q.colabtaskmanagement.dataaccess.repository;

import com.q.colabtaskmanagement.dataaccess.model.WorkspaceInvites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitesRepository extends JpaRepository<WorkspaceInvites, UUID> {
    boolean existsByWorkspaceIdAndUserIdAndAcceptedIsTrue(UUID workspaceId, UUID userId);

    Optional<WorkspaceInvites> findByWorkspaceIdAndUserIdAndAcceptedIsFalse(UUID workspaceId, UUID userId);
}
