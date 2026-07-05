package io.github.bartlomiejgora.trucks;

public interface TruckService {

    void addTruck(Truck truck);

    void update(Truck truck);

    Truck getOne(String vin);
}
