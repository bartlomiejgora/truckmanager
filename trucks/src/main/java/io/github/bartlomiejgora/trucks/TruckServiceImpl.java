package io.github.bartlomiejgora.trucks;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

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
                       .ifPresent(found -> {
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
                                found.getMileage(), found.getDriverId())
        ).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public Truck removeDriver(UUID id) {
        var truck = truckRepository.findByDriverId(id);
        truck.ifPresent(t -> {
            t.setDriverId(null);
            truckRepository.save(t);
        });

        return truck.map(t -> new Truck(t.getVendor(), t.getVin(), t.getPlateNumber(),
                t.getMileage(), t.getDriverId())).orElse(null);
    }
}
