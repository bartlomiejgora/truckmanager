package io.github.bartlomiejgora.trucks;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
class Truck {
    private final Vendor vendor;
    @Setter
    private String plateNumber;
    @Setter
    private double mileage;

}
