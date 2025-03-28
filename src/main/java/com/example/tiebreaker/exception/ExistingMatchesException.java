package com.example.tiebreaker.exception;

import com.example.tiebreaker.dto.MatchDTO;

import java.util.List;

public class ExistingMatchesException extends RuntimeException {

    private final List<MatchDTO> existingMatches;

    public ExistingMatchesException(List<MatchDTO> matchDTOList) {
        existingMatches = matchDTOList;
    }

    public List<MatchDTO> getExistingMatches() {
        return existingMatches;
    }
}
