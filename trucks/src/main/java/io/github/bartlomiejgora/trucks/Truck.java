package io.github.bartlomiejgora.trucks;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Truck entity")
public class Truck {
    @NotNull
    private Vendor vendor;
    @NotBlank
    private  String vin;
    @Schema(description = "License plate number", example = "WA 12345")
    private String plateNumber;
    @Schema(description = "Total mileage in kilometers", example = "150000.5")
    private Double mileage;

}
