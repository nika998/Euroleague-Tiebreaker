package com.example.tiebreaker.model;

public class Game {

    private Team local;

    private Team road;

    private boolean played;

    public Game() {
    }

    public Game(Game game) {
        this.local = new Team(game.getLocal());
        this.road = new Team(game.getRoad());
        played = true;
    }

    public Team getLocal() {
        return local;
    }

    public void setLocal(Team local) {
        this.local = local;
    }

    public Team getRoad() {
        return road;
    }

    public void setRoad(Team road) {
        this.road = road;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String getHomeTeamCode() {
        return local.getClub().getCode();
    }

    public void setHomeTeamCode(String homeTeamCode) {
        local.getClub().setCode(homeTeamCode);
    }

    public String getAwayTeamCode() {
        return road.getClub().getCode();
    }

    public void setAwayTeamCode(String awayTeamCode) {
        road.getClub().setCode(awayTeamCode);
    }

    public int getHomeTeamScore() {
        return local.getScore();
    }

    public int getHomeTeamStandingsScore() {
        return local.getStandingsScore();
    }

    public void setHomeTeamScore(int homeTeamScore) {
        local.setScore(homeTeamScore);
    }

    public void setHomeTeamStandingsScore(int homeTeamStandingsScore) {
        local.setStandingsScore(homeTeamStandingsScore);
    }

    public int getAwayTeamScore() {
        return road.getScore();
    }

    public int getAwayTeamStandingsScore() {
        return road.getStandingsScore();
    }

    public void setAwayTeamScore(int awayTeamScore) {
        road.setScore(awayTeamScore);
    }

    public void setAwayTeamStandingsScore(int awayTeamStandingsScore) {
        road.setStandingsScore(awayTeamStandingsScore);
    }

    public static class Team {

        private Club club;

        private int score;

        private int standingsScore;

        public Team() {
        }

        public Team(Team team) {
            this.club = new Club(team.getClub());
            this.score = team.getScore();
            this.standingsScore = team.getStandingsScore();

        }

        public Club getClub() {
            return club;
        }

        public void setClub(Club club) {
            this.club = club;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getStandingsScore() {
            return standingsScore;
        }

        public void setStandingsScore(int standingsScore) {
            this.standingsScore = standingsScore;
        }
    }

    public static class Club {
        private String code;

        private String name;

        public Club() {
        }

        public Club(Club club) {
            this.code = club.getCode();
            this.name = club.getName();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
