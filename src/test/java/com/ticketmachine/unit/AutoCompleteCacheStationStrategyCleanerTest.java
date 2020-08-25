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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {TicketMachineApplication.class}, properties = { "cache.automplete.timeOfExpiration=1000", "cache.automplete.cleanUpPeriod=1001" })
class AutoCompleteCacheStationStrategyCleanerTest {

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
    void shouldRemoveExpiredValue() {
        final String key = "DART";
        StationDTO dto = new StationDTO(nextChars, nextWords);
        cache.add(key, dto);
        long time = Long.parseLong(env.getProperty("cache.automplete.timeOfExpiration"));
        sleep(time);
        StationDTO cacheValue = cache.get(key);
        assertNull(cacheValue);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}