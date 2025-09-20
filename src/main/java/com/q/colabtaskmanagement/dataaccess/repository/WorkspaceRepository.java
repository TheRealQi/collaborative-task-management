package com.q.colabtaskmanagement.dataaccess.repository;


import com.q.colabtaskmanagement.dataaccess.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
