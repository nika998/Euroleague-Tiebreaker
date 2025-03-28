package com.example.tiebreaker.dto;

import java.util.List;

public record PredictionRankingsReqDTO(List<String> teams, List<MatchPredictionDTO> predictedMatches) {
}
