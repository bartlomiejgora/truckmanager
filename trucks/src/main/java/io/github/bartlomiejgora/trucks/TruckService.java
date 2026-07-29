package io.github.bartlomiejgora.trucks;

import java.util.UUID;

public interface TruckService {

    void addTruck(Truck truck);

    void update(Truck truck);

    Truck getOne(String vin);

    Truck removeDriver(UUID id);
}
