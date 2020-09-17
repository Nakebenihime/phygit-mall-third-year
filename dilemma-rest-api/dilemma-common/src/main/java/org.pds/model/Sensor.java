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
@Document(collection = "sensor")
@AllArgsConstructor
public class Sensor {
    @Id
    private String sensorId;
    @NotBlank(message = "UUID cannot be empty")
    private String UUID;
    @NotBlank(message = "storeId cannot be empty")
    private String storeId;

    @PersistenceConstructor
    public Sensor(@NotBlank(message = "UUID cannot be empty") String UUID, @NotBlank(message = "storeId cannot be empty") String storeId) {
        this.UUID = UUID;
        this.storeId = storeId;
    }
}
