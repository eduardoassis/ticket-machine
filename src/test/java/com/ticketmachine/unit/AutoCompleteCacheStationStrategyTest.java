package com.ticketmachine.unit;

import com.ticketmachine.TicketMachineApplication;
import com.ticketmachine.cache.ICache;
import com.ticketmachine.dtos.StationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TicketMachineApplication.class})
class AutoCompleteCacheStationStrategyTest {

    @Autowired
    private Environment env;

    @Autowired
    private ICache<StationDTO> cache;

    private String dartford = "DARTFORD",
            dartmouth = "DARTMOUTH";

    private List<String> nextWords;
    private List<Character> nextChars;

    @BeforeEach
    public void beforeEach() {
        nextWords = Arrays.asList(dartford, dartmouth);
        nextChars = Arrays.asList('F', 'M');
        cache.clear();
    }

    @Test
    void shouldAddKeyValue() {
        String key = "DAR";
        nextChars = Arrays.asList('T');
        StationDTO dto = new StationDTO(nextChars, nextWords);
        cache.add(key, dto);

        key = "DART";
        assertNotNull(cache.get(key));

        assertEquals(cache.get(key).getPossibleWords().size(), dto.getPossibleWords().size());
        assertEquals(cache.get(key).getPossibleNextChars().size(), dto.getPossibleNextChars().size());
    }

    @Test
    void shouldNotAddWithNullKey() {
        final String key = null;
        StationDTO dto = new StationDTO(nextChars, nextWords);
        cache.add(key, dto);
        assertTrue(cache.isEmpty());
    }

    @Test
    void shouldNotAddWithNullValueAndShouldRemoveKey() {
        final String key = "DART";
        StationDTO dto = new StationDTO(nextChars, nextWords);
        cache.add(key, dto);
        assertNotNull(cache.get(key + "M"));
        dto = null;
        cache.add(key, dto);
        assertNull(cache.get(key + "M"));
    }

    @Test
    void shouldReturnNullWhenKeyIsNull() {
        String key = "DAR";
        nextChars = Arrays.asList('T');
        StationDTO dto = new StationDTO(nextChars, nextWords);
        cache.add(key, dto);
        key = null;
        assertNull(cache.get(key));
    }

}