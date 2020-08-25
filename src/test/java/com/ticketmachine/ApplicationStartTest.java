package com.ticketmachine;

import com.ticketmachine.dtos.StationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TicketMachineApplication.class}, properties = { "server.port=9091" })
@AutoConfigureMockMvc
class ApplicationStartTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void applicationStarts() throws Exception {
        TicketMachineApplication.main(new String[] {});
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/actuator/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}