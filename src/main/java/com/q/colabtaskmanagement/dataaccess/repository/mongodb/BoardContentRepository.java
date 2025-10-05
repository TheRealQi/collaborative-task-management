package com.q.colabtaskmanagement.dataaccess.repository.mongodb;

import com.q.colabtaskmanagement.dataaccess.model.mongodb.BoardContent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardContentRepository extends MongoRepository<BoardContent, ObjectId> {
    Optional<BoardContent> findByBoardId(UUID boardId);
    void deleteByBoardId(UUID boardId);

}
