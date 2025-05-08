package com.mezzala.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RankingDto {

    private int rankingId;
    private int season;
    // competition
    private int competitionId;
    // team
    private int teamId;
    private int matchCount;
    private int point;
    private int win;
    private int draw;
    private int lose;
    private int scorePoint;
    private int losePoint;

    CompetitionDto competition;
    TeamDto team;

}
