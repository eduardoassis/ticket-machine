package com.ticketmachine.services;

import com.ticketmachine.dtos.StationDTO;
import com.ticketmachine.models.Station;

import java.util.List;

public interface AutocompleteService {
    StationDTO autocomplete(final String word);
    StationDTO updateAutocomplete(final String word, final StationDTO oldValue);
    List<Station> listAll();
}