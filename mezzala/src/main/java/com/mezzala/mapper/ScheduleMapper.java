package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import com.mezzala.dto.ScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<ScheduleDto> selectSchedule(@Param("fromDay") String fromDay, @Param("tillDay") String tillDay);
}
