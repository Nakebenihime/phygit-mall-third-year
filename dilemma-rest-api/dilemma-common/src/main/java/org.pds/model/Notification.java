package org.pds.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notification")
public class Notification {
    @Id
    private String id;
    private String title;
    private String body;
    private boolean hasBeenSent;
    private int TTL;

    @PersistenceConstructor
    public Notification(@JsonProperty("title") String title,
                        @JsonProperty("body") String body,
                        @JsonProperty("hasBeenSent") boolean hasBeenSent,
                        @JsonProperty("TTL") int TTL) {
        this.title = title;
        this.body = body;
        this.hasBeenSent = hasBeenSent;
        this.TTL = TTL;
    }
}