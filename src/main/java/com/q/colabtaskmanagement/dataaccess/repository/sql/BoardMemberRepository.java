package com.q.colabtaskmanagement.dataaccess.repository.sql;

import com.q.colabtaskmanagement.common.enums.BoardRole;
import com.q.colabtaskmanagement.dataaccess.model.sql.BoardMember;
import com.q.colabtaskmanagement.dataaccess.model.id.BoardMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberId> {

    Optional<BoardMember> findByIdBoardIdAndIdUserId(UUID boardId, UUID userId);

    List<BoardMember> findByIdBoardId(UUID boardId);

    long countByIdBoardIdAndRole(UUID boardId, BoardRole role);

    void deleteAllByIdBoardId(UUID boardId);
}
