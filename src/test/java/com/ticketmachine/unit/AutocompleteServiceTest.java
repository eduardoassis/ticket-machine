package com.ticketmachine.unit;

import com.ticketmachine.TicketMachineApplication;
import com.ticketmachine.builders.StationBuilder;
import com.ticketmachine.dtos.StationDTO;
import com.ticketmachine.models.Station;
import com.ticketmachine.repositories.StationRepository;
import com.ticketmachine.services.AutocompleteService;
import com.ticketmachine.services.impl.AutocompleteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TicketMachineApplication.class}, properties = { "cache.automplete.timeOfExpiration=2000", "cache.automplete.cleanUpPeriod=2001" })
@ExtendWith(MockitoExtension.class)
class AutocompleteServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private AutocompleteService stationService = new AutocompleteServiceImpl();

    private String dartford = "DARTFORD",
            dartmouth = "DARTMOUTH",
            derby = "DERBY",
            camaraGaia = "CÂMARA GAIA",
            camaraMatosinhos = "CÂMARA MATOSINHOS";

    @Test
    void shouldAutocomplete() {
        when(stationRepository.autocomplete(anyString())).thenReturn(Arrays.asList(dartford, dartmouth, derby));
        StationDTO result = stationService.autocomplete("D");
        assertEquals(3, result.getPossibleWords().size());
        assertEquals(2, result.getPossibleNextChars().size());
        verify(stationRepository, times(1)).autocomplete(anyString());
    }

    @Test
    void shouldUpdateAutocompleteWithoutFetchInDB() {

        when(stationRepository.autocomplete(anyString())).thenReturn(Arrays.asList(dartford, dartmouth, derby));

        StationDTO result = stationService.autocomplete("D");
        assertEquals(3, result.getPossibleWords().size());
        assertEquals(2, result.getPossibleNextChars().size());

        StationDTO updatedResult = stationService.updateAutocomplete("DA", result);
        assertEquals(2, updatedResult.getPossibleWords().size());
        assertEquals(1, updatedResult.getPossibleNextChars().size());
        verify(stationRepository, times(1)).autocomplete(anyString());
    }

    @Test
    void shouldUpdateAutocompleteWithSamePossibleWords() {

        when(stationRepository.autocomplete(anyString())).thenReturn(Arrays.asList(dartford, dartmouth));

        StationDTO result = stationService.autocomplete("DA");
        assertEquals(2, result.getPossibleWords().size());
        assertEquals(1, result.getPossibleNextChars().size());

        StationDTO updatedResult = stationService.updateAutocomplete("DAR", result);
        assertEquals(2, updatedResult.getPossibleWords().size());
        assertEquals(1, updatedResult.getPossibleNextChars().size());
        verify(stationRepository, times(1)).autocomplete(anyString());
    }

    @Test
    void shouldAutocompleteConsideringWhiteSpace() {

        when(stationRepository.autocomplete(anyString())).thenReturn(Arrays.asList(camaraGaia, camaraMatosinhos));

        String input = "CÂMARA ";

        StationDTO dto = stationService.autocomplete(input);

        assertFalse(dto.getPossibleNextChars().isEmpty());
        assertFalse(dto.getPossibleWords().isEmpty());
        assertEquals(2, dto.getPossibleWords().size());
        assertEquals(2, dto.getPossibleNextChars().size());
        assertTrue(dto.getPossibleWords().contains(camaraGaia));
        assertTrue(dto.getPossibleWords().contains(camaraMatosinhos));
        assertTrue(dto.getPossibleNextChars().contains('M'), "M is expected as a possible next value");
        assertTrue(dto.getPossibleNextChars().contains('G'), "G is expected as a possible next value");
    }

    @Test
    void shouldAutocompleteConsideringWhiteSpaceAsNextCharacter() {

        when(stationRepository.autocomplete(anyString())).thenReturn(Arrays.asList(camaraGaia, camaraMatosinhos));

        String input = "CÂMARA";

        StationDTO dto = stationService.autocomplete(input);

        assertFalse(dto.getPossibleNextChars().isEmpty(), "Expected possible next characters, but was empty.");
        assertFalse(dto.getPossibleWords().isEmpty(), "Expected next possible words, but was empty.");
        assertEquals(2, dto.getPossibleWords().size(), "Expected 2 possible words");
        assertEquals(1, dto.getPossibleNextChars().size(), "Expected 1 possible next character");
        assertTrue(dto.getPossibleWords().contains(camaraGaia));
        assertTrue(dto.getPossibleWords().contains(camaraMatosinhos));
        assertTrue(dto.getPossibleNextChars().contains(' '), "' ' is expected as a possible next value");
    }

    @Test
    void shouldListAll() {

        Station dartfordStation = new StationBuilder().withName(dartford).build();
        Station dartmouthStation = new StationBuilder().withName(dartmouth).build();
        Station derbyStation = new StationBuilder().withName(derby).build();
        Station camaraGaiaStation = new StationBuilder().withName(camaraGaia).build();
        Station camaraMatosinhosStation = new StationBuilder().withName(camaraMatosinhos).build();

        when(stationRepository.findAll())
                .thenReturn(Arrays.asList(dartfordStation, dartmouthStation, derbyStation, camaraGaiaStation, camaraMatosinhosStation));

        List<Station> list = stationService.listAll();

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(5, list.size());
    }
}
