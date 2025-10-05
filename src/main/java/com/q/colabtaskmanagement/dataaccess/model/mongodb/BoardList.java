package com.q.colabtaskmanagement.dataaccess.model.mongodb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardList {
    private ObjectId id = new ObjectId();
    private String title;
    private int position;
}
