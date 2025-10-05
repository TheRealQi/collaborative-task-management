package com.q.colabtaskmanagement.dataaccess.repository.sql;

import com.q.colabtaskmanagement.dataaccess.model.sql.User_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User_, UUID> {

    @Query("SELECT u FROM User_ u WHERE u.username = :input OR u.email = :input")
    Optional<User_> findByUsernameOrEmail(@Param("input") String usernameOrEmail);

    @Query("SELECT u.id FROM User_ u WHERE u.username = :input OR u.email = :input")
    Optional<UUID> findIdByUsernameOrEmail(@Param("input") String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
