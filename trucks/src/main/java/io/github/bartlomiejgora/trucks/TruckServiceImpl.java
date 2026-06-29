package io.github.bartlomiejgora.trucks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;

    @Override
    public void addTruck(final Truck truck) {
        truckRepository.save(TruckDocument.of(truck));
    }

    @Override
    public void update(String id, Truck truck) {

    }
}
