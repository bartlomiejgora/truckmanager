package io.github.bartlomiejgora.trucks;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String vin;
    @Setter
    private String plateNumber;
    @Setter
    private double mileage;

    static TruckDocument of(Truck other) {
        return new TruckDocument(UUID.randomUUID().toString(), other.getVendor(), other.getPlateNumber(),
                other.getVin(),
                other.getMileage());
    }

}
