package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RankingDto {

    private int rankingId;
    private int season;
    // competition
    private int competitionId;
    // team
    private int teamId;
    private int win;
    private int draw;
    private int lose;
    private int scorePoint;
    private int losePoint;

    CompetitionDto competition;
    TeamDto team;

}
