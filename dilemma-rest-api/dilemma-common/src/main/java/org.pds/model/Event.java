package org.pds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@Data
@Document(collection = "events")
@AllArgsConstructor
public class Event {
    @Id
    private ObjectId eventId;
    private String user;
    private String detector;
}