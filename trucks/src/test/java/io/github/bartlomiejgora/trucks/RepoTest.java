package io.github.bartlomiejgora.trucks;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RepoTest extends AbstractTestBase{

    @Autowired
    TruckRepository truckRepository;

    @Test
    void testDB(){
        //given
        var id = UUID.randomUUID().toString();
        Truck truck = new Truck(id, Vendor.IVECO);
        truck.setPlateNumber("SL 90890J");

        //when
        truckRepository.save(truck);

        //then

        var result = truckRepository.findById(id);
        Assertions.assertThat(result).isNotEmpty();


    }
}
