package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleRequestDto {

    private ScheduleDto schedule;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

}
