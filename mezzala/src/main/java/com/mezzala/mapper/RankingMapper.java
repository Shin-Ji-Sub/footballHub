package com.mezzala.mapper;

import com.mezzala.dto.CompetitionDto;
import com.mezzala.dto.RankingDto;
import com.mezzala.dto.ScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingMapper {

    List<CompetitionDto> selectAllCompetition();

    List<RankingDto> selectRanking(@Param("competitionId") int competitionId, @Param("season") int season,
                                   @Param("limit") int limit);
}
