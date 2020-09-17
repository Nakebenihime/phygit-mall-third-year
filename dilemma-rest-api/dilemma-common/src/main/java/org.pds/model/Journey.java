package org.pds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@NoArgsConstructor
@Data
@Document(collection = "journey")
@AllArgsConstructor
public class Journey {
    @Id
    private String journeyId;
    private List<Vertex> nodes;
}
