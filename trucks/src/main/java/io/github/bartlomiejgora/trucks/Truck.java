package io.github.bartlomiejgora.trucks;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Schema(description = "Truck entity")
public class Truck {
    @NotNull
    private final Vendor vendor;
    @NotBlank
    private final String vin;
    @Setter
    @Schema(description = "License plate number", example = "WA 12345")
    private String plateNumber;
    @Setter
    @Schema(description = "Total mileage in kilometers", example = "150000.5")
    private double mileage;

}
