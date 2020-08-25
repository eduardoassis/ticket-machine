package com.ticketmachine.dtos;

import java.util.List;

public class StationDTO {

    private List<Character> possibleNextChars;
    private List<String> possibleWords;

    public StationDTO(List<Character> possibleNextChars, List<String> possibleWords) {
        this.possibleNextChars = possibleNextChars;
        this.possibleWords = possibleWords;
    }

    public List<Character> getPossibleNextChars() {
        return possibleNextChars;
    }

    public List<String> getPossibleWords() {
        return possibleWords;
    }

}