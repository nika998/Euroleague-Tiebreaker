package com.example.tiebreaker.dto;

public record TeamScoreDTO(String teamCode, String teamName, int headToHeadWins, int headToHeadPointsDifference,
                           int totalPointsDifference, int totalPointsScored) {

    public TeamScoreDTO(String teamCode, String teamName, int totalPointsDifference, int totalPointsScored) {
        this(teamCode, teamName, 0, 0, totalPointsDifference, totalPointsScored);
    }

    public TeamScoreDTO withUpdatedStats(int additionalWins, int additionalPointsDifference) {
        return new TeamScoreDTO(
                this.teamCode,
                this.teamName,
                this.headToHeadWins + additionalWins,
                this.headToHeadPointsDifference + additionalPointsDifference,
                this.totalPointsDifference,
                this.totalPointsScored);
    }
}
