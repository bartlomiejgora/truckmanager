package io.github.bartlomiejgora.trucks;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
class Truck {
    @MongoId
    private final String uuid;
    private final Vendor vendor;
    @Setter
    private String plateNumber;
    @Setter
    private double mileage;

}
