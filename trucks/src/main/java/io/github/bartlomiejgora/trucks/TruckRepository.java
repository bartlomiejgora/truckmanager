package io.github.bartlomiejgora.trucks;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

interface TruckRepository extends MongoRepository<Truck, String> {
}
