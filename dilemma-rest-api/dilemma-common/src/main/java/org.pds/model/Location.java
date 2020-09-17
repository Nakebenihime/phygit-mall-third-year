package org.pds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@Data
@Document(collection = "location")
@AllArgsConstructor
public class Location {
    @Id
    private String locationId;
    @NotBlank(message = "X value of the location cannot be blank")
    private double x;
    @NotBlank(message = "Y value of the location cannot be blank")
    private double y;
    @NotBlank(message = "Floor value of the location cannot be blank")
    private int floor;
}
