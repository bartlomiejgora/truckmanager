package io.github.bartlomiejgora.trucks;

import org.springframework.data.mongodb.repository.MongoRepository;

interface TruckRepository extends MongoRepository<TruckDocument, String> {
}
