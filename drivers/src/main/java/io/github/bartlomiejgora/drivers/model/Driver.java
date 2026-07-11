package io.github.bartlomiejgora.drivers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
class Driver {
    @Id
    UUID id;
    String firstName;
    String lastName;

}
