package io.github.bartlomiejgora.trucks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class RepoTest extends AbstractTestBase {

    @Autowired
    TruckRepository truckRepository;

    @Test
    void testDB() {

        //given
        var vin = "01234567890";
        Truck truck = new Truck();
        truck.setVendor(Vendor.IVECO);
        truck.setVin(vin);
        truck.setPlateNumber("SL 90890J");
        var document = TruckDocument.of(truck);

        //when
        truckRepository.save(document);

        //then
        var result = truckRepository.findById(document.getUuid());
        assertThat(result).isNotEmpty();
        var actual = result.get();
        assertThat(actual.getUuid()).isEqualTo(document.getUuid());

    }
}
