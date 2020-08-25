package com.ticketmachine.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketmachine.builders.StationBuilder;
import com.ticketmachine.dtos.StationDTO;
import com.ticketmachine.models.Station;
import com.ticketmachine.repositories.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StationControllerTest {

    private static final String BASE_PATH = "/autocomplete";
    public static final String BASE_URI = "https://localhost";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    public void beforeEach() {
        stationRepository.deleteAll();
        Station stationCarolinaMichaelis = new StationBuilder().withName("Carolina michaelis").build();
        Station stationCasaDaMusica = new StationBuilder().withName("CASA DA MUSICA").build();
        Station stationCamaraGaia = new StationBuilder().withName("CAMARA GAIA").build();
        Station stationCamaraMatosinhos = new StationBuilder().withName("CAMARA MATOSINHOS").build();
        stationRepository.saveAll(Arrays.asList(stationCarolinaMichaelis, stationCamaraGaia, stationCasaDaMusica, stationCamaraMatosinhos));
    }

    @Test
    void shouldAutocomplete() throws Exception {

        final String word = "CAMARA ";

        AtomicReference<StationDTO> atomicReference = new AtomicReference<>();
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/autocomplete/{word}", word)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    atomicReference.set(objectMapper.readValue(json, StationDTO.class));
                }
        );

        assertNotNull(atomicReference.get());
        StationDTO dto = atomicReference.get();

        assertEquals(2, dto.getPossibleNextChars().size());
        assertEquals(2, dto.getPossibleWords().size());

        assertTrue(dto.getPossibleWords().contains("CAMARA GAIA"));
        assertTrue(dto.getPossibleWords().contains("CAMARA MATOSINHOS"));
        assertTrue(dto.getPossibleNextChars().contains('G'));
        assertTrue(dto.getPossibleNextChars().contains('M'));
    }

    @Test
    void shouldReturnResultFasterInSecondTime() throws Exception {

        String word = "CAMA";

        long start = System.currentTimeMillis();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/autocomplete/{word}", word)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        long end = System.currentTimeMillis();
        long timeFirstCall = end - start;

        word = "CAMAR";
        start = System.currentTimeMillis();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/autocomplete/{word}", word)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        end = System.currentTimeMillis();
        long timeSecondCall = end - start;

        assertTrue(timeSecondCall < timeFirstCall);
    }

}