package com.q.colabtaskmanagement.dataaccess.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "board_contents")
public class BoardContent {
    @Id
    private ObjectId id;
    private UUID boardId;
    private List<BoardList> lists;
}
