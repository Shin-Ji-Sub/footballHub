package com.mezzala.service;

import com.mezzala.dto.CompetitionDto;
import com.mezzala.dto.RankingDto;
import com.mezzala.dto.ScheduleDto;

import java.util.List;
import java.util.Map;

public interface RankingService {

    List<CompetitionDto> findAllCompetition();

    List<RankingDto> findRanking(int competitionId, int season, int limit);
}
