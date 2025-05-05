package com.mezzala.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ScheduleDto {

    private int scheduleId;
    private Date scheduleDate;
    // competition
    private int competitionId;
    // competition_round
    private int roundId;
    private int homeTeamId;
    private int awayTeamId;

    CompetitionDto competition;
    CompetitionRoundDto competitionRound;
    TeamDto homeTeam;
    TeamDto awayTeam;

}
