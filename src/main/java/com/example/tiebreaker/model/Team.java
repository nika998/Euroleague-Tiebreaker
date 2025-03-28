package com.example.tiebreaker.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "team")
public class Team {

    @JacksonXmlProperty(localName = "code")
    private String teamCode;

    @JacksonXmlProperty(localName = "name")
    private String teamName;

    @JacksonXmlProperty(localName = "ptsfavour")
    private Integer pointsScored;

    @JacksonXmlProperty(localName = "difference")
    private Integer pointsDifference;

    public Team() {
    }

    public Team(Team other) {
        this.teamCode = other.teamCode;
        this.teamName = other.teamName;
        this.pointsScored = other.pointsScored;
        this.pointsDifference = other.pointsDifference;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public Integer getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(Integer pointsScored) {
        this.pointsScored = pointsScored;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getPointsDifference() {
        return pointsDifference;
    }

    public void setPointsDifference(Integer pointsDifference) {
        this.pointsDifference = pointsDifference;
    }
}
