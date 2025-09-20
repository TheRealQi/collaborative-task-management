package com.q.colabtaskmanagement.dataaccess.repository;

import com.q.colabtaskmanagement.dataaccess.model.User_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User_, UUID> {

    @Query("SELECT u FROM User_ u WHERE u.username = :input OR u.email = :input")
    Optional<User_> findUserByUsernameOrEmail(@Param("input") String usernameOrEmail);

    @Query("SELECT u.id FROM User_ u WHERE u.username = :input OR u.email = :input")
    UUID findIdByUsernameOrEmail(@Param("input") String usernameOrEmail);

    Optional<User_> findUserById(UUID id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
