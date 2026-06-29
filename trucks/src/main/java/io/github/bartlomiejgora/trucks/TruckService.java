package io.github.bartlomiejgora.trucks;

public interface TruckService {

    void addTruck(Truck truck);

    void update(String id, Truck truck);
}
