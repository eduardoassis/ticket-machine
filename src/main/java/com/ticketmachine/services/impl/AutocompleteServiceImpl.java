package com.ticketmachine.services.impl;


import com.ticketmachine.dtos.StationDTO;
import com.ticketmachine.models.Station;
import com.ticketmachine.repositories.StationRepository;
import com.ticketmachine.services.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutocompleteServiceImpl implements AutocompleteService {

    @Override
    public StationDTO autocomplete(String word) {
        List<String> words = stationRepository.autocomplete(word);
        return convertToDTO(words, word);
    }

    @Autowired
    public StationRepository stationRepository;

    @Override
    public StationDTO updateAutocomplete(final String word, final StationDTO oldValue) {
        List<Character> possiblesCharacters = updateNextPossiblesCharacters(oldValue.getPossibleWords(), word);
        List<String> possibleWords = updatePossibleWords(oldValue.getPossibleWords(), word);
        return convertToDTO(possiblesCharacters, possibleWords);
    }

    @Override
    public List<Station> listAll() {
        return stationRepository.findAll();
    }

    private StationDTO convertToDTO(final List<Character> characters, final List<String> words) {
        return new StationDTO(characters, words);
    }

    private StationDTO convertToDTO(final List<String> words, final String word) {
        return new StationDTO(updateNextPossiblesCharacters(words, word), words);
    }

    private List<Character> updateNextPossiblesCharacters(final List<String> words, final String word) {
        return words
                .parallelStream()
                .filter(s -> s.toUpperCase().contains(word.toUpperCase()) && !s.equalsIgnoreCase(word))
                .map(s -> s.toUpperCase().replace(word.toUpperCase(), "").charAt(0))
                .collect(Collectors.toList())
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> updatePossibleWords(final List<String> words, final String word) {
        return words
                .stream()
                .filter(s -> s.toUpperCase().contains(word.substring(0, word.length()).replaceAll("\\s+$", "").toUpperCase()))
                .collect(Collectors.toList());
    }

}