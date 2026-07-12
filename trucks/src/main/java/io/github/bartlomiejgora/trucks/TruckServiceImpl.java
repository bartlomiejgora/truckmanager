package io.github.bartlomiejgora.trucks;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        var foundTruck = truckRepository.findFirstByVin(truck.getVin());
        if (truck.getMileage() != null) {
            foundTruck.setMileage(truck.getMileage());
        }
        if (StringUtils.isNotBlank(truck.getPlateNumber())){
            foundTruck.setPlateNumber(truck.getPlateNumber());
        }
        truckRepository.save(foundTruck);
    }

    @Override
    public Truck getOne(String vin) {
        var foundTruck = truckRepository.findFirstByVin(vin);
        return new Truck(foundTruck.getVendor(), foundTruck.getVin(), foundTruck.getPlateNumber(),
                foundTruck.getMileage());
    }
}
