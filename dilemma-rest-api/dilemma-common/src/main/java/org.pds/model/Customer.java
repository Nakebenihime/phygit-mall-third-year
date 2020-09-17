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
@Document(collection = "customer")
@AllArgsConstructor
public class Customer {
    @Id
    private String customerId;
    @NotBlank(message = "firstName cannot be empty")
    private String firstName;
    @NotBlank(message = "lastName cannot be empty")
    private String lastName;
    @NotBlank(message = "profile cannot be empty")
    private String profile;

    public Customer(String firstName, String lastName, String profile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile = profile;
    }
}
