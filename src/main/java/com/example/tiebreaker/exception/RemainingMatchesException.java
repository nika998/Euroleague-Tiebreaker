package com.example.tiebreaker.exception;

import com.example.tiebreaker.dto.MatchDTO;

import java.util.List;

public class RemainingMatchesException extends RuntimeException {

    private final List<MatchDTO> remainingMatches;

    public RemainingMatchesException(List<MatchDTO> matchDTOList) {
        remainingMatches = matchDTOList;
    }

    public List<MatchDTO> getRemainingMatches() {
        return remainingMatches;
    }
}
