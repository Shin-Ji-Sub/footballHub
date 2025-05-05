package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.dto.*;
import com.mezzala.service.AdminService;
import com.mezzala.service.ScheduleService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = {"/schedule"})
public class ScheduleController {

    @Setter(onMethod_ = {@Autowired})
    private ScheduleService scheduleService;

    @GetMapping(path = {"/table"})
    public String table() {
        return "/scheduleTable/scheduleTable";
    }

    @GetMapping(path = {"/get-schedule"})
    public String getSchedule(Model model,
                              @RequestParam(name = "year") int year,
                              @RequestParam(name = "month") int month,
                              @RequestParam(name = "day") int day) {

        try {

            Map<Integer, List<ScheduleDto>> schedules = scheduleService.findSchedule(year, month, day);
            model.addAttribute("schedules", schedules);

            if (schedules.isEmpty()) {
                return "/modules/noDataModule";
            }

        } catch (Exception e) {
            return "/modules/noDataModule";
        }


        return "/scheduleTable/modules/scheduleList";
    }

}
