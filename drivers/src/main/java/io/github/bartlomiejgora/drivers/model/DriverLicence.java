package io.github.bartlomiejgora.drivers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
class DriverLicence {
    @Id
    UUID id;
    @ManyToOne(targetEntity = Driver.class)
    UUID driverId;
    OffsetDateTime starDate;
    OffsetDateTime expirationDate;

}
