package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import com.mezzala.dto.ScheduleDto;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    Map<Integer, List<ScheduleDto>> findSchedule(int year, int month, int day);
}
