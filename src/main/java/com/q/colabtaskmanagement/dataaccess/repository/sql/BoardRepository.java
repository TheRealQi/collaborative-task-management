package com.q.colabtaskmanagement.dataaccess.repository.sql;
import com.q.colabtaskmanagement.common.dto.board.BoardDTO;
import com.q.colabtaskmanagement.dataaccess.model.sql.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    @Query("SELECT new com.q.colabtaskmanagement.common.dto.board.BoardDTO(" +
            "b.id, b.title, bm.role, b.visibility,b.workspace.id) " +
            "FROM BoardMember bm " +
            "JOIN bm.board b " +
            "WHERE bm.user.id = :userId")
    List<BoardDTO> findBoardsByUserId(@Param("userId") UUID userId);

    @Query("SELECT new com.q.colabtaskmanagement.common.dto.board.BoardDTO(" +
            "b.id, b.title, bm.role, b.visibility, b.workspace.id) " +
            "FROM BoardMember bm " +
            "JOIN bm.board b " +
            "WHERE b.workspace.id = :workspaceId")
    List<BoardDTO> findAllBoardsByWorkspaceId(UUID workspaceId);
}
