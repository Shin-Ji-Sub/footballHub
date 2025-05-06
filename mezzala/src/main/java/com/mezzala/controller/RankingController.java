package com.mezzala.controller;

import com.mezzala.dto.ScheduleDto;
import com.mezzala.service.ScheduleService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = {"/ranking"})
public class RankingController {

    @Setter(onMethod_ = {@Autowired})
    private ScheduleService scheduleService;

    @GetMapping(path = {"/table"})
    public String table() {
        return "/rankingTable/rankingTable";
    }

}
