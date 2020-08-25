package com.ticketmachine.cache;

import com.ticketmachine.dtos.StationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutoCompleteCacheStationStrategy implements ICache<StationDTO> {

    @Value( "${cache.automplete.timeOfExpiration}" )
    private long timeOfExpiration;

    @Value( "${cache.automplete.cleanUpPeriod}" )
    private long cleanUpPeriod;

    private final Logger logger = LoggerFactory.getLogger(AutoCompleteCacheStationStrategy.class);

    private final Map<String, CacheValue<StationDTO>> map = new HashMap<>();

    public AutoCompleteCacheStationStrategy() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(cleanUpPeriod);
                    map.entrySet().removeIf(entry -> entry.getValue().isExpired());
                } catch (Exception e) {
                    logger.debug("Error in cleanerThread. ", e);
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    @Override
    public void add(final String key, final StationDTO value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            map.remove(key);
        } else if (map.containsKey(key.substring(0, key.length() - 1))) {
                map.remove(key.substring(0, key.length() - 1));
        }
        map.put(key, new CacheValue<>(value, timeOfExpiration));
    }

    @Override
    public StationDTO get(final String key) {

        if (null == key)
            return null;

        String k = key.toUpperCase();
        CacheValue<StationDTO> value = map.get(k.substring(0, k.length()-1));
        if (value != null && !value.isExpired()) {
            return value.getValue();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public void clear() {
        this.map.clear();
    }
}