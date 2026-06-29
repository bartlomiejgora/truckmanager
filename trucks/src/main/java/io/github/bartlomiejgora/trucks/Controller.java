package io.github.bartlomiejgora.trucks;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class Controller {

    private final TruckService truckService;

    @PostMapping("/truck")
    void createTruck(@RequestBody Truck truck) {
        truckService.saveTruck(truck);
    }
}
