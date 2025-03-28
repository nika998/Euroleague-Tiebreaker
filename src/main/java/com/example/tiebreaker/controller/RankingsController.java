package com.example.tiebreaker.controller;

import com.example.tiebreaker.dto.PredictionRankingsReqDTO;
import com.example.tiebreaker.dto.TeamScoreDTO;
import com.example.tiebreaker.dto.TeamDTO;
import com.example.tiebreaker.service.RankingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rankings")
public class RankingsController {

    private final RankingsService standingsService;

    public RankingsController(RankingsService standingsService) {
        this.standingsService = standingsService;
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeams() {
        return ResponseEntity.of(Optional.ofNullable(standingsService.getTeams()));
    }

    @GetMapping
    public ResponseEntity<List<TeamScoreDTO>> getRankings(@RequestParam("teams") List<String> teamsToCompare) {
        return ResponseEntity.of(Optional.ofNullable(standingsService.getRankings(teamsToCompare)));
    }

    @PostMapping
    public ResponseEntity<List<TeamScoreDTO>> getPredictionRankings(@RequestBody PredictionRankingsReqDTO predictionRankingsReq) {
        return ResponseEntity.of(Optional.ofNullable(standingsService.getPredictionRankings(predictionRankingsReq.teams(), predictionRankingsReq.predictedMatches())));
    }

}
