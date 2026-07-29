package io.github.bartlomiejgora.trucks;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(
            summary = "Get a truck by VIN number",
            description = "Returns a single truck matching the provided VIN number."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Truck found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Truck.class),
                            examples = @ExampleObject(
                                    name = "Truck response",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456",
                                              "plateNumber": "WA 12345",
                                              "mileage": 150000.5
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Truck not found",
                    content = @Content
            )
    })
    @GetMapping("truck/{vin}")
    ResponseEntity<Truck> getTruck(@Parameter(
            description = "VIN number of the truck",
            example = "YV2RT40A5XA123456",
            required = true
    )
                                   @PathVariable String vin) {
        var truck = truckService.getOne(vin);
        if (truck != null) {
            return ResponseEntity.ok(truck);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Remove Driver from Truck"

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Truck found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Truck.class),
                            examples = @ExampleObject(
                                    name = "Truck response",
                                    value = """
                                            {
                                              "vendor": "VOLVO",
                                              "vin": "YV2RT40A5XA123456",
                                              "plateNumber": "WA 12345",
                                              "mileage": 150000.5
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Truck not found",
                    content = @Content
            )
    })
    @DeleteMapping("trucks/{id}/driver")
    ResponseEntity<Truck> deleteDriver(@Parameter(description = "Id of driver", required = true) @PathVariable UUID id) {
        var truck = truckService.removeDriver(id);
        if (truck != null) {
            return ResponseEntity.ok(truck);
        }
        return ResponseEntity.notFound().build();
    }
}
