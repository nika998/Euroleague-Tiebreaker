package com.example.tiebreaker.persistence;

import com.example.tiebreaker.feign.EuroleagueClient;
import com.example.tiebreaker.model.Game;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class GamesStorage {

    private static final Logger logger = LoggerFactory.getLogger(GamesStorage.class);

    private final EuroleagueClient euroleagueClient;

    private List<Game> games;

    public GamesStorage(EuroleagueClient euroleagueClient) {
        this.euroleagueClient = euroleagueClient;
    }

    @PostConstruct
    public void init() {
        populateGamesStorage();
    }

    @CacheEvict(value = "rankings", allEntries = true)
    @Scheduled(cron = "0 0 22,1 * * TUE,WED,THU,FRI")
    public void populateGamesStorage() {
        games = euroleagueClient.getGames().getData();
        logger.info("Games storage updated on {}", LocalDateTime.now());
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Game> getPlayedGames() {
        return games.stream()
                .filter(Game::isPlayed)
                .toList();
    }

    public Game findGameByTeams(String homeTeamCode, String awayTeamCode) {
        if (games == null) {
            return null;
        }
        var resultList = games.stream()
                .filter(game -> game.getHomeTeamCode().equals(homeTeamCode) && game.getAwayTeamCode().equals(awayTeamCode) && game.isPlayed())
                .toList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.getFirst();
        }
    }
}
