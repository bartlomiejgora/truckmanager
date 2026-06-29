package io.github.bartlomiejgora.trucks;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document
class TruckDocument {
    @MongoId
    private String uuid;
    private Vendor vendor;
    @Setter
    private String plateNumber;
    @Setter
    private double mileage;

    static TruckDocument of(Truck other) {
        return new TruckDocument(UUID.randomUUID().toString(), other.getVendor(), other.getPlateNumber(),
                other.getMileage());
    }

}
