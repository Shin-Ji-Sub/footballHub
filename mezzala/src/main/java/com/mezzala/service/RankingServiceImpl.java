package com.mezzala.service;

import com.mezzala.dto.CompetitionDto;
import com.mezzala.dto.RankingDto;
import com.mezzala.dto.ScheduleDto;
import com.mezzala.mapper.RankingMapper;
import com.mezzala.mapper.ScheduleMapper;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankingServiceImpl implements RankingService {
    @Setter
    private RankingMapper rankingMapper;

    @Override
    public List<CompetitionDto> findAllCompetition() {
        return rankingMapper.selectAllCompetition();
    }

    @Override
    public List<RankingDto> findRanking(int competitionId, int season, int limit) {
        return rankingMapper.selectRanking(competitionId, season, limit);
    }
}
