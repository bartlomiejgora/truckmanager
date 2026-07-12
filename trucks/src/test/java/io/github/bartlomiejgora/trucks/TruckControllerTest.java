package io.github.bartlomiejgora.trucks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TruckControllerTest extends AbstractTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TruckRepository truckRepository;

    @BeforeEach
    void setUp() {
        truckRepository.deleteAll();
    }

    // POST /truck

    @Test
    void createTruck_withAllFields_returns200() throws Exception {
        // given
        var payload = """
                {"vendor": "VOLVO", "vin": "YV2RT40A5XA123456", "plateNumber": "WA 12345", "mileage": 150000.5}
                """;

        // when
        mockMvc.perform(post("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // then
        assertThat(truckRepository.findFirstByVin("YV2RT40A5XA123456")).isNotNull();
    }

    @Test
    void createTruck_withRequiredFieldsOnly_returns200() throws Exception {
        // given
        var payload = """
                {"vendor": "SCANIA", "vin": "XLR0000010V123456"}
                """;

        // when / then
        mockMvc.perform(post("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void createTruck_withMissingVendor_returns400() throws Exception {
        // given
        var payload = """
                {"vin": "YV2RT40A5XA123456"}
                """;

        // when / then
        mockMvc.perform(post("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTruck_withBlankVin_returns400() throws Exception {
        // given
        var payload = """
                {"vendor": "VOLVO", "vin": ""}
                """;

        // when / then
        mockMvc.perform(post("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    // PATCH /truck

    @Test
    void updateTruck_updatesMileage_returns200() throws Exception {
        // given
        var truck = new Truck();
        truck.setVendor(Vendor.MERCEDES);
        truck.setVin("WDB9634031L123456");
        truckRepository.save(TruckDocument.of(truck));

        var payload = """
                {"vendor": "MERCEDES", "vin": "WDB9634031L123456", "mileage": 250000.0}
                """;

        // when
        mockMvc.perform(patch("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // then
        var updated = truckRepository.findFirstByVin("WDB9634031L123456");
        assertThat(updated).isNotEmpty();
        var updatedValue = updated.get();
        assertThat(updatedValue.getMileage()).isEqualTo(250000.0);
    }

    @Test
    void updateTruck_updatesPlateNumber_returns200() throws Exception {
        // given
        var truck = new Truck();
        truck.setVendor(Vendor.IVECO);
        truck.setVin("ZCFC35A00LV123456");
        truckRepository.save(TruckDocument.of(truck));

        var payload = """
                {"vendor": "IVECO", "vin": "ZCFC35A00LV123456", "plateNumber": "KR 99999"}
                """;

        // when
        mockMvc.perform(patch("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // then
        var updated = truckRepository.findFirstByVin("ZCFC35A00LV123456");
        assertThat(updated).isNotEmpty();
        var updatedValue = updated.get();
        assertThat(updatedValue.getPlateNumber()).isEqualTo("KR 99999");
    }

    @Test
    void updateTruck_withBlankVin_returns400() throws Exception {
        // given
        var payload = """
                {"vendor": "RENAULT", "vin": ""}
                """;

        // when / then
        mockMvc.perform(patch("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    // GET /truck/{vin}

    @Test
    void getTruck_existingVin_returns200WithBody() throws Exception {
        // given
        var truck = new Truck();
        truck.setVendor(Vendor.VOLVO);
        truck.setVin("YV2RT40A5XA123456");
        truck.setPlateNumber("WA 12345");
        truckRepository.save(TruckDocument.of(truck));

        // when / then
        mockMvc.perform(get("/truck/YV2RT40A5XA123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("YV2RT40A5XA123456"))
                .andExpect(jsonPath("$.vendor").value("VOLVO"));
    }

    @Test
    void getTruck_nonExistingVin_returns404() throws Exception {
        mockMvc.perform(get("/truck/NONEXISTENTVIN12345"))
                .andExpect(status().isNotFound());
    }
}
