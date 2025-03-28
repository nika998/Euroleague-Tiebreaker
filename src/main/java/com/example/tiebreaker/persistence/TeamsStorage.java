package com.example.tiebreaker.persistence;

import com.example.tiebreaker.feign.EuroleagueClient;
import com.example.tiebreaker.model.Team;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TeamsStorage {

    private static final Logger logger = LoggerFactory.getLogger(TeamsStorage.class);

    private final EuroleagueClient euroleagueClient;

    private Map<String, Team> teams;

    public TeamsStorage(EuroleagueClient euroleagueClient) {
        this.euroleagueClient = euroleagueClient;
    }

    @PostConstruct
    public void init() {
        populateTeamsStorage();
    }

    @CacheEvict(value = "rankings", allEntries = true)
    @Scheduled(cron = "0 0 22,1 * * TUE,WED,THU,FRI")
    public void populateTeamsStorage() {
        teams = euroleagueClient.getTeams().getGroups().getFirst().getTeams().stream()
                .collect(Collectors.toMap(
                        Team::getTeamCode,
                        team -> team
                ));
        logger.info("Teams storage updated on {}", LocalDateTime.now());
    }

    public Map<String, Team> getTeams() {
        return teams;
    }
}
