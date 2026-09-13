package io.github.bartlomiejgora.trucks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.bartlomiejgora.trucks.TestJwts.bezRoli;
import static io.github.bartlomiejgora.trucks.TestJwts.kierowca;
import static io.github.bartlomiejgora.trucks.TestJwts.wlascicielFirmy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TruckSecurityTest extends AbstractTestBase {

    private static final String VIN = "VF624BPA000123456";

    private static final String PAYLOAD = """
            {"vendor": "RENAULT", "vin": "VF624BPA000123456", "plateNumber": "PO 11111"}
            """;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TruckRepository truckRepository;

    @BeforeEach
    void setUp() {
        truckRepository.deleteAll();
    }

    @Test
    void getTruck_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/truck/" + VIN))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTruck_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/truck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTruck_asWlascicielFirmy_returns200() throws Exception {
        mockMvc.perform(post("/truck")
                        .with(wlascicielFirmy())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isOk());
    }

    @Test
    void createTruck_asKierowca_returns403() throws Exception {
        mockMvc.perform(post("/truck")
                        .with(kierowca())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateTruck_asKierowca_returns403() throws Exception {
        mockMvc.perform(patch("/truck")
                        .with(kierowca())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTruck_asKierowca_isAllowed() throws Exception {
        // given
        var truck = new Truck();
        truck.setVendor(Vendor.RENAULT);
        truck.setVin(VIN);
        truckRepository.save(TruckDocument.of(truck));

        // when / then
        mockMvc.perform(get("/truck/" + VIN).with(kierowca()))
                .andExpect(status().isOk());
    }

    @Test
    void getTruck_withTokenWithoutRoles_returns403() throws Exception {
        mockMvc.perform(get("/truck/" + VIN).with(bezRoli()))
                .andExpect(status().isForbidden());
    }
}
