package io.github.bartlomiejgora.trucks;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class Controller {

    private final TruckService truckService;

    @Operation(summary = "Create a new truck")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Truck.class),
                    examples = {
                            @ExampleObject(
                                    name = "Truck with mileage",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456",
                                              "plateNumber": "WA 12345",
                                              "mileage": 150000.5
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Truck without mileage",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456",
                                              "plateNumber": "WA 12345"
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Truck without plateNumber",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456",
                                              "mileage": 150000.5
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Truck with required fields",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456"
                                            }"""
                            )
                    }
            )
    )
    @PostMapping("/truck")
    void createTruck(@Valid @RequestBody Truck truck) {
        truckService.addTruck(truck);
    }

    @Operation(summary = "Updates Truck")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Truck.class),
                    examples = {
                            @ExampleObject(name = "Default value",
                                    value = """
                                              {
                                                "vendor": "VOLVO",
                                                "vin": "YV2RT40A5XA123456",
                                                "plateNumber": "WA 12345",
                                                "mileage": 150000.5
                                              }
                                            """)
                    }
            )
    )
    @PatchMapping("/truck")
    void updateTruck(@Valid @RequestBody Truck truck) {
        truckService.update(truck);

    }

    @GetMapping("truck/{vin}")
    Truck getTruck(@PathVariable String vin){
        return null;
    }
}
