package com.example.tiebreaker.feign;

import com.example.tiebreaker.model.Results;
import com.example.tiebreaker.model.Standings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "euroleagueClient", url = "${euroleague.api.url}")
public interface EuroleagueClient {

    @Value("${euroleague.api.season-code}")
    String SEASON_CODE = "E2024";

    @Value("${euroleague.api.competition-code}")
    String COMPETITION_CODE = "E";

    @GetMapping(value = "/v2/competitions/" + COMPETITION_CODE + "/seasons/" + SEASON_CODE + "/games", produces = "application/json")
    Results getGames();

    @GetMapping(value = "/v1/standings?seasonCode=" + SEASON_CODE, produces = "application/xml")
    Standings getTeams();
}
