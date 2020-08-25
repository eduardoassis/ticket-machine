package com.ticketmachine.services;

import com.ticketmachine.dtos.StationDTO;

public interface AutocompleteService {
    StationDTO autocomplete(final String word);
    StationDTO updateAutocomplete(final String word, final StationDTO oldValue);
}