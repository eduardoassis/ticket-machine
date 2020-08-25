package com.ticketmachine.controllers;


import com.ticketmachine.cache.ICache;
import com.ticketmachine.dtos.StationDTO;
import com.ticketmachine.models.Station;
import com.ticketmachine.services.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StationController {

    @Autowired
    private AutocompleteService stationService;
    @Autowired
    public ICache<StationDTO> cache;

    @GetMapping(value = "/autocomplete/{word}", produces = "application/json")
    public ResponseEntity<StationDTO> autocomplete(@PathVariable(required = true) final String word) {

        StationDTO cacheValue = cache.get(word);

        if (null != cacheValue) {
            StationDTO newValue = stationService.updateAutocomplete(word, cacheValue);
            cache.add(word, newValue);
            return ResponseEntity.ok(newValue);
        }

        StationDTO dto = stationService.autocomplete(word);
        cache.add(word, dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/stations", produces = "application/json")
    public ResponseEntity<List<Station>> listAll() {
        return ResponseEntity.ok(stationService.listAll());
    }
}