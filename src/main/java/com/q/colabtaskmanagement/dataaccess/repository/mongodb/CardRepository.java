package com.q.colabtaskmanagement.dataaccess.repository.mongodb;

import com.q.colabtaskmanagement.dataaccess.model.mongodb.Card;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends MongoRepository<Card, ObjectId> {
    void deleteAllByBoardId(UUID boardId);

    void deleteAllByListId(ObjectId listId);

    List<Card> findAllByBoardId(UUID boardId);

    long countByListId(ObjectId listId);

    @Modifying
    @Query("{ 'listId': ?0, 'position': { $gt: ?1 } }")
    @Update("{ $inc: { 'position': -1 } }")
    void decrementPositionsAfter(ObjectId listId, int fromPosition);

    @Modifying
    @Query("{ 'listId': ?0, 'position': { $gte: ?1 } }")
    @Update("{ $inc: { 'position': 1 } }")
    void incrementPositionsFrom(ObjectId listId, int fromPosition);

    @Modifying
    @Query("{ 'listId': ?0, 'position': { $gte: ?1, $lte: ?2 } }")
    @Update("{ $inc: { 'position': -1 } }")
    void decrementPositionsBetween(ObjectId listId, int startPos, int endPos);

    @Modifying
    @Query("{ 'listId': ?0, 'position': { $gte: ?1, $lte: ?2 } }")
    @Update("{ $inc: { 'position': 1 } }")
    void incrementPositionsBetween(ObjectId listId, int startPos, int endPos);
}
