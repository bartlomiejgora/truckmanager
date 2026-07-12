package io.github.bartlomiejgora.trucks;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;

    @Override
    public void addTruck(final Truck truck) {
        truckRepository.save(TruckDocument.of(truck));
    }

    @Override
    @Transactional
    public void update(Truck truck) {
        truckRepository.findFirstByVin(truck.getVin())
                .ifPresent( found -> {
                    if (truck.getMileage() != null) {
                        found.setMileage(truck.getMileage());
                    }
                    if (StringUtils.isNotBlank(truck.getPlateNumber())) {
                        found.setPlateNumber(truck.getPlateNumber());
                    }
                    truckRepository.save(found);
                });


    }

    @Override
    public Truck getOne(String vin) {
        return truckRepository.findFirstByVin(vin).map(
                found ->
                        new Truck(found.getVendor(), found.getVin(), found.getPlateNumber(),
                                found.getMileage())
        ).orElseThrow(NoSuchElementException::new);
    }
}
