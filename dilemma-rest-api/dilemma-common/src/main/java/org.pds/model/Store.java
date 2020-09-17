package org.pds.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@Data
@Document(collection = "store")
@AllArgsConstructor
public class Store {

    @Id
    private String storeId;
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "longitude cannot be empty")
    private Double longitude;
    @NotBlank(message = "latitude cannot be empty")
    private Double latitude;
    @NotBlank(message = "type cannot be empty")
    private String type;

    @PersistenceConstructor
    public Store(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
