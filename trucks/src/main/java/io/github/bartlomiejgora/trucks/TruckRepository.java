package io.github.bartlomiejgora.trucks;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

interface TruckRepository extends MongoRepository<TruckDocument, String> {

    Optional<TruckDocument> findFirstByVin(String vin);

    Optional<TruckDocument> findByDriverId(UUID driverId);
}
