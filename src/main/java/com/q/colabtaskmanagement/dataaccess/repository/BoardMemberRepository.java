package com.q.colabtaskmanagement.dataaccess.repository;

import com.q.colabtaskmanagement.dataaccess.model.BoardMember;
import com.q.colabtaskmanagement.dataaccess.model.WorkspaceMember;
import com.q.colabtaskmanagement.dataaccess.model.id.BoardMemberId;
import com.q.colabtaskmanagement.dataaccess.model.id.WorkspaceMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberId> {
}
