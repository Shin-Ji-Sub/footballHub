package com.mezzala.dto;

import lombok.Data;

@Data
public class CompetitionDto {

    private int competitionId;
    private String competitionName;
    private String competitionCategory;
    private int championsLeagueFrom;
    private int championsLeagueTo;
    private int otherLeagueFrom;
    private int otherLeagueTo;

}
