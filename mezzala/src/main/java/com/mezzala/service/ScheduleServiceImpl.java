package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import com.mezzala.dto.ScheduleDto;
import com.mezzala.mapper.RequestMapper;
import com.mezzala.mapper.ScheduleMapper;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleServiceImpl implements ScheduleService {
    @Setter
    private ScheduleMapper scheduleMapper;

    @Override
    public Map<Integer, List<ScheduleDto>> findSchedule(int year, int month, int day) {
        LocalDate fromDate = LocalDate.of(year, month, day);
        LocalDate tillDate = fromDate.plusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDay = fromDate.format(formatter);
        String tillDay = tillDate.format(formatter);

        List<ScheduleDto> schedules = scheduleMapper.selectSchedule(fromDay, tillDay);

        Map<Integer, List<ScheduleDto>> groupedSchedules = schedules.stream()
                .collect(Collectors.groupingBy(ScheduleDto::getCompetitionId));

        return groupedSchedules;
    }
}
