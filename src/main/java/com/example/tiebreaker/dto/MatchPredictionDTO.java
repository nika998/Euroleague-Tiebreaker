package com.example.tiebreaker.dto;

public record MatchPredictionDTO(TeamDTO homeTeam, TeamDTO awayTeam, int homePoints, int awayPoints) {
}
