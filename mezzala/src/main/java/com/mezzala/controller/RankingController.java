package com.mezzala.controller;

import com.mezzala.dto.CompetitionDto;
import com.mezzala.dto.RankingDto;
import com.mezzala.dto.ScheduleDto;
import com.mezzala.service.RankingService;
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
    private RankingService rankingService;

    @GetMapping(path = {"/table"})
    public String table(Model model) {
        List<CompetitionDto> competitions = rankingService.findAllCompetition();
        model.addAttribute("competitions", competitions);
        return "rankingTable/rankingTable";
    }

    @GetMapping(path = {"/get-ranking"})
    public String getRanking(Model model,
                             @RequestParam(name = "competitionId") int competitionId,
                             @RequestParam(name = "season") int season,
                             @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        try {

            int pageSize = 10;
            int limit = pageSize * pageNo;
            List<RankingDto> rankings = rankingService.findRanking(competitionId, season, limit);

            if (rankings.isEmpty()) {
                return "modules/noDataModule";
            }

            model.addAttribute("rankings", rankings);

        } catch (Exception e) {
            return "modules/noDataModule";
        }

        return "rankingTable/modules/rankingList";
    }

}
