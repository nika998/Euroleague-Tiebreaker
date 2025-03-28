package com.example.tiebreaker.service;

import com.example.tiebreaker.dto.MatchPredictionDTO;
import com.example.tiebreaker.dto.MatchDTO;
import com.example.tiebreaker.dto.TeamScoreDTO;
import com.example.tiebreaker.dto.TeamDTO;
import com.example.tiebreaker.exception.ExistingMatchesException;
import com.example.tiebreaker.exception.RemainingMatchesException;
import com.example.tiebreaker.model.Game;
import com.example.tiebreaker.model.Team;
import com.example.tiebreaker.persistence.GamesStorage;
import com.example.tiebreaker.persistence.TeamsStorage;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingsService {

    private final TeamsStorage teamsStorage;

    private final GamesStorage gamesStorage;

    public RankingsService(TeamsStorage teamsStorage, GamesStorage gamesStorage) {
        this.teamsStorage = teamsStorage;
        this.gamesStorage = gamesStorage;
    }

    @Cacheable(value = "rankings", key = "#teamsToCompare.stream().sorted().collect(T(java.util.stream.Collectors).joining(','))")
    public List<TeamScoreDTO> getRankings(List<String> teamsToCompare) {
        var remainingMatches = findRemainingMatches(teamsToCompare);
        if (!remainingMatches.isEmpty()) {
            throw new RemainingMatchesException(remainingMatches);
        }
        return getRankings(teamsToCompare, gamesStorage.getPlayedGames(), teamsStorage.getTeams());
    }

    public List<TeamScoreDTO> getPredictionRankings(List<String> teamsToCompare, List<MatchPredictionDTO> predictedMatches) {
        var games = new ArrayList<>(gamesStorage.getPlayedGames().stream().map(Game::new).toList());

        var existingMatches = findExistingMatches(predictedMatches);
        if (!existingMatches.isEmpty()) {
            throw new ExistingMatchesException(existingMatches);
        }

        return getRankings(teamsToCompare, getGamesDataWithPredictions(games, predictedMatches), getTeamsDataWithPredictions(teamsStorage.getTeams(), predictedMatches));
    }

    public List<TeamScoreDTO> getRankings(List<String> teamsToCompare, List<Game> games, Map<String, Team> teamsData) {
        var teamStats = teamsToCompare.stream()
                .collect(Collectors.toMap(
                        team -> team,
                        team -> new TeamScoreDTO(team, teamsData.get(team).getTeamName(),
                                teamsData.get(team).getPointsDifference(), teamsData.get(team).getPointsScored())

                ));

        for (Game game : games) {
            var homeTeamCode = game.getHomeTeamCode();
            var awayTeamCode = game.getAwayTeamCode();

            if (teamsToCompare.contains(homeTeamCode) && teamsToCompare.contains(awayTeamCode)) {

                var homeScore = game.getHomeTeamScore();
                var awayScore = game.getAwayTeamScore();

                var homeStandingScore = game.getHomeTeamStandingsScore();
                var awayStandingScore = game.getAwayTeamStandingsScore();

                var homeTeam = teamStats.get(homeTeamCode);
                var awayTeam = teamStats.get(awayTeamCode);

                TeamScoreDTO updatedHomeTeamStats;
                TeamScoreDTO updatedAwayTeamStats;

                var homeAdditionalPoints = Math.subtractExact(homeStandingScore, awayStandingScore);
                var awayAdditionalPoints = Math.subtractExact(awayStandingScore, homeStandingScore);

                if (homeScore > awayScore) {
                    updatedHomeTeamStats = homeTeam
                            .withUpdatedStats(1, homeAdditionalPoints);
                    updatedAwayTeamStats = awayTeam
                            .withUpdatedStats(0, awayAdditionalPoints);
                } else {
                    updatedAwayTeamStats = awayTeam
                            .withUpdatedStats(1, awayAdditionalPoints);
                    updatedHomeTeamStats = homeTeam
                            .withUpdatedStats(0, homeAdditionalPoints);
                }

                teamStats.put(awayTeamCode, updatedAwayTeamStats);
                teamStats.put(homeTeamCode, updatedHomeTeamStats);
            }
        }

        return teamStats.values().stream().sorted(Comparator.comparing(TeamScoreDTO::headToHeadWins).reversed()
                        .thenComparing(Comparator.comparingInt(TeamScoreDTO::headToHeadPointsDifference).reversed())
                        .thenComparing(Comparator.comparingInt(TeamScoreDTO::totalPointsDifference).reversed())
                        .thenComparing(Comparator.comparingInt(TeamScoreDTO::totalPointsScored).reversed()))
                .toList();
    }

    private List<MatchDTO> findRemainingMatches(List<String> teamsToCompare) {
        var remainingMatches = new ArrayList<MatchDTO>();
        teamsToCompare.forEach(team ->
                teamsToCompare.forEach(t -> {
                    if (!team.equals(t)) {
                        var playedGame = gamesStorage.findGameByTeams(team, t);
                        if (playedGame == null) {
                            remainingMatches.add(new MatchDTO(getTeamByCode(team), getTeamByCode(t)));
                        }
                    }
                })
        );
        return remainingMatches;
    }

    private List<MatchDTO> findExistingMatches(List<MatchPredictionDTO> predictedMatches) {
        var existingMatches = new ArrayList<MatchDTO>();
        predictedMatches.forEach(match -> {
                    var existingMatch = getGameByTeams(match);
                    if (existingMatch != null) {
                        existingMatches.add(new MatchDTO(match.homeTeam(), match.awayTeam()));
                    }
                }
        );
        return existingMatches;
    }

    private Map<String, Team> getTeamsDataWithPredictions(Map<String, Team> teams, List<MatchPredictionDTO> predictedMatches) {
        var teamsData = teams.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Team(entry.getValue())
                ));

        predictedMatches.forEach(match -> {
            var homeTeam = teamsData.get(match.homeTeam().code());
            var awayTeam = teamsData.get(match.awayTeam().code());

            homeTeam.setPointsScored(homeTeam.getPointsScored() + match.homePoints());
            homeTeam.setPointsDifference(homeTeam.getPointsDifference() + Math.subtractExact(match.homePoints(), match.awayPoints()));

            awayTeam.setPointsScored(awayTeam.getPointsScored() + match.awayPoints());
            awayTeam.setPointsDifference(awayTeam.getPointsDifference() + Math.subtractExact(match.awayPoints(), match.homePoints()));

            teamsData.put(homeTeam.getTeamCode(), homeTeam);
            teamsData.put(awayTeam.getTeamCode(), awayTeam);
        });
        return teamsData;
    }

    private List<Game> getGamesDataWithPredictions(List<Game> games, List<MatchPredictionDTO> predictedMatches) {
        games.addAll(predictedMatches.stream().map(match -> {
            var game = new Game();
            var local = new Game.Team();
            var road = new Game.Team();
            local.setClub(new Game.Club());
            road.setClub(new Game.Club());
            game.setLocal(local);
            game.setRoad(road);

            game.setAwayTeamCode(match.awayTeam().code());
            game.setAwayTeamScore(match.awayPoints());
            game.setAwayTeamStandingsScore(match.awayPoints());
            game.setHomeTeamCode(match.homeTeam().code());
            game.setHomeTeamScore(match.homePoints());
            game.setHomeTeamStandingsScore(match.homePoints());

            return game;
        }).toList());
        return games;
    }

    public List<TeamDTO> getTeams() {
        return teamsStorage.getTeams().values().stream().map(t -> new TeamDTO(t.getTeamCode(), t.getTeamName())).toList();
    }

    public TeamDTO getTeamByCode(String teamCode) {
        var team = teamsStorage.getTeams().get(teamCode);
        return new TeamDTO(teamCode, team.getTeamName());
    }

    public Game getGameByTeams(MatchPredictionDTO predictedMatch) {
        return gamesStorage.findGameByTeams(predictedMatch.homeTeam().code(), predictedMatch.awayTeam().code());
    }
}

