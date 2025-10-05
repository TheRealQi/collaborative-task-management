package com.q.colabtaskmanagement.dataaccess.repository.sql;


import com.q.colabtaskmanagement.dataaccess.model.sql.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
