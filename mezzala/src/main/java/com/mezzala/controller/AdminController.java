package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.dto.*;
import com.mezzala.mapper.AdminMapper;
import com.mezzala.service.AdminService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = {"/admin"})
public class AdminController {

    @Setter(onMethod_ = {@Autowired})
    private AdminService adminService;

    @GetMapping(path = {"/notice"})
    public String notice(Model model,
                         @RequestParam(name = "to", defaultValue = "all") String to,
                         @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                         @RequestParam(name = "from", defaultValue = "admin") String from,
                         @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {

        model.addAttribute("to", to);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("from", from);
        model.addAttribute("searchValue", searchValue);
        return "/admin/notice/notice";
    }

    @GetMapping(path = {"/write-notice"})
    public String writeNotice() {
        return "/admin/notice/writeNotice";
    }

    @PostMapping(path = {"/write-board"})
    @ResponseBody
    public String writeBoard(BoardDto board, @RequestParam(name = "ImageFilesArr", required = false) String ImageFilesArr) {
        List<Map<String, String>> imageFiles = new ArrayList<>();
        if (ImageFilesArr != null && !ImageFilesArr.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                imageFiles = objectMapper.readValue(ImageFilesArr, new TypeReference<List<Map<String, String>>>() {
                });
                System.out.println("이미지 리스트: " + imageFiles);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        adminService.addBoard(board, imageFiles);

        return "success";
    }

    @GetMapping(path = {"/content-list"})
    public String contentList(Model model, HttpServletRequest req, HttpSession session,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                              @RequestParam(name = "state") boolean state,
                              @RequestParam(name = "from") String from) {
        // paging
        int pageSize = 0;
        if (from.equals("currentNoticeList")) {
            pageSize = 5;
        }
        if (from.equals("pastNoticeList")) {
            pageSize = 10;
        }
        int pagerSize = 5;
        int dataCount = adminService.findAllNoticeBoardCount(searchValue, state);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = adminService.findNoticeBoardWithPaging(start, searchValue, state);
        if (boards.isEmpty()) {
            pageNo = pageNo - 1;
            start = pageSize * (pageNo - 1);
            pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
            try {
                boards = adminService.findNoticeBoardWithPaging(start, searchValue, state);
            } catch (Exception e) {
                return "/modules/noDataModule";
            }
        }

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "/admin/notice/modules/" + from;
    }

    @PostMapping(path = {"/control-notice"})
    @ResponseBody
    public String controlNotice(@RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "state") boolean state) {

        adminService.modifyBoardState(boardId, state);

        return "success";
    }

    @GetMapping(path = {"/content"})
    public String content(Model model, @RequestParam(name = "boardId") int boardId,
                          @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                          @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                          @RequestParam(name = "from") String from,
                          @RequestParam(name = "to", required = false) String to,
                          @CookieValue(value = "visited", required = false) String visitedBoard,
                          HttpServletResponse res, HttpSession session) {
        // 쿠키에 현재 boardId가 포함되어 있는지 확인
        if (visitedBoard == null || !visitedBoard.contains("[" + boardId + "]")) {
            adminService.incrementVisitedBoard(boardId); // 조회수 증가

            // 쿠키에 현재 boardId 추가
            visitedBoard = (visitedBoard == null ? "" : visitedBoard) + "[" + boardId + "]";
            Cookie cookie = new Cookie("visited", visitedBoard);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간(1일)
            res.addCookie(cookie);
        }

        List<BoardDto> boards = adminService.findNoticeBoardWithBoardId(boardId);
        BoardDto board = boards.get(0);

        model.addAttribute("board", board);

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "/admin/notice/content";
    }

    @PostMapping(path = {"/save-notice"})
    @ResponseBody
    public String saveNotice(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> contentIds = request.get("contentIds");
        adminService.modifyBoardsState(contentIds);
        return "success";
    }

    @GetMapping(path = {"/board-manage"})
    public String boardManage(Model model,
                              @RequestParam(name = "to", defaultValue = "boardSearch") String to,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                              @RequestParam(name = "totalSelectValue", defaultValue = "all") String totalSelectValue,
                              @RequestParam(name = "smallSelectValue", defaultValue = "all") String smallSelectValue,
                              @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                              @RequestParam(name = "reportCategory", defaultValue = "board") String reportCategory) {

        model.addAttribute("to", to);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("totalSelectValue", totalSelectValue);
        model.addAttribute("smallSelectValue", smallSelectValue);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("reportCategory", reportCategory);

        return "/admin/board-manage/board-manage";
    }

    @GetMapping(path = {"/get-content"})
    public String getContent(Model model, HttpServletRequest req,
                             @RequestParam(name = "to", defaultValue = "boardSearch") String to,
                             @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                             @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                             @RequestParam(name = "totalSelectValue", defaultValue = "all") String totalSelectValue,
                             @RequestParam(name = "smallSelectValue", defaultValue = "all") String smallSelectValue,
                             @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                             @RequestParam(name = "reportCategory", defaultValue = "board") String reportCategory,
                             @RequestParam(name = "categoryPart", defaultValue = "large") String categoryPart,
                             @RequestParam(name = "largeCategoryValue", defaultValue = "1") int largeCategoryValue) {

//        try {
        if (to.equals("boardSearch")) {
            // paging
            int pageSize = 10;
            int pagerSize = 5;
            int dataCount = adminService.findAllBoardCount(totalSelectValue, smallSelectValue, searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            int start = pageSize * (pageNo - 1);

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
            List<BoardDto> boards = adminService.findBoardWithPaging(start, sortValue, totalSelectValue, smallSelectValue, searchValue);

            if (boards.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);

            model.addAttribute("boards", boards);
        }
        if (to.equals("reportList") && reportCategory.equals("board")) {
            // paging
            int pageSize = 10;
            int pagerSize = 5;
            int dataCount = adminService.findAllReportBoardCount(searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            int start = pageSize * (pageNo - 1);

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
            List<BoardDto> boards = adminService.findReportBoardWithPaging(start, sortValue, searchValue);

            if (boards.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);

            model.addAttribute("boards", boards);
        } else if (to.equals("reportList") && reportCategory.equals("comment")) {
            // paging
            int pageSize = 10;
            int pagerSize = 5;
            int dataCount = adminService.findAllReportBoardCommentCount(searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            int start = pageSize * (pageNo - 1);

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
            List<CommentDto> comments = adminService.findReportBoardCommentWithPaging(start, sortValue, searchValue);

            System.out.println("[COMMENTS] : " + comments);

            if (comments.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);

            model.addAttribute("comments", comments);

            return "/admin/board-manage/modules/commentList";
        }
        if (to.equals("controlCategory")) {
            // paging
            int pageSize = 10;
            int pagerSize = 5;
            int dataCount = adminService.findAllCategoryCount(categoryPart, largeCategoryValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            int start = pageSize * (pageNo - 1);

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

            if (categoryPart.equals("large")) {
                List<BoardLargeCategoryDto> largeCategories = adminService.findLargeCategory(start);
                if (largeCategories.isEmpty()) {
                    return "/modules/noDataModule";
                }

                model.addAttribute("largeCategories", largeCategories);
            }

            if (categoryPart.equals("small")) {
                List<BoardSmallCategoryDto> smallCategories = adminService.findSmallCategory(start, largeCategoryValue);
                if (smallCategories.isEmpty()) {
                    return "/modules/noDataModule";
                }

                model.addAttribute("smallCategories", smallCategories);
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);
            model.addAttribute("categoryPart", categoryPart);

            return "/admin/board-manage/modules/categoryList";

        }

        return "/admin/board-manage/modules/contentList";
//        } catch (Exception e) {
//            return "/modules/noDataModule";
//        }

    }

    @GetMapping(path = {"/detail-content"})
    public String detailContent(Model model,
                                @RequestParam(name = "to", defaultValue = "boardSearch") String to,
                                @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                                @RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "totalSelectValue", defaultValue = "all") String totalSelectValue,
                                @RequestParam(name = "smallSelectValue", defaultValue = "all") String smallSelectValue,
                                @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                                @RequestParam(name = "reportCategory", defaultValue = "board") String reportCategory) {

        List<BoardDto> boards = adminService.findBoardWithBoardId(boardId);
        BoardDto board = boards.get(0);

        model.addAttribute("to", to);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("totalSelectValue", totalSelectValue);
        model.addAttribute("smallSelectValue", smallSelectValue);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("reportCategory", reportCategory);
        model.addAttribute("board", board);

        return "/admin/board-manage/content";
    }

    @GetMapping(path = {"/get-controller"})
    public String getController(Model model,
                                @RequestParam(name = "to", defaultValue = "boardSearch") String to,
                                @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                                @RequestParam(name = "totalSelectValue", defaultValue = "all") String totalSelectValue,
                                @RequestParam(name = "smallSelectValue", defaultValue = "all") String smallSelectValue,
                                @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                                @RequestParam(name = "reportCategory", defaultValue = "board") String reportCategory) {

//        System.out.println(pageNo);
//        System.out.println("to : " + to);
//        System.out.println("sort : " + sortValue);
//        System.out.println("total : " + totalSelectValue);
//        System.out.println("small : " + smallSelectValue);
//        System.out.println("search : " + searchValue);

        if (to.equals("boardSearch")) {
            List<BoardLargeCategoryDto> largeCategories = adminService.findCategories();

            model.addAttribute("largeCategories", largeCategories);

            model.addAttribute("sortValue", sortValue);
            if (totalSelectValue.equals("all")) {
                model.addAttribute("totalSelectValue", totalSelectValue);
            } else {
                model.addAttribute("totalSelectValue", Integer.parseInt(totalSelectValue));
            }
            if (smallSelectValue.equals("all")) {
                model.addAttribute("smallSelectValue", smallSelectValue);
            } else {
                model.addAttribute("smallSelectValue", Integer.parseInt(smallSelectValue));
            }
            model.addAttribute("searchValue", searchValue);
        }
        if (to.equals("reportList")) {
            model.addAttribute("sortValue", sortValue);
            model.addAttribute("searchValue", searchValue);
            model.addAttribute("reportCategory", reportCategory);
        }
        if (to.equals("controlCategory")) {
            List<BoardLargeCategoryDto> largeCategories = adminService.findAllLargeCategory();
            model.addAttribute("largeCategories", largeCategories);
        }

        return "/admin/board-manage/modules/" + to + "Module";

    }

    @PostMapping(path = {"/add-category"})
    @ResponseBody
    public String addCategory(@RequestParam(name = "part") String part,
                              @RequestParam(name = "categoryValue") String categoryValue,
                              @RequestParam(name = "largeCategoryValue") String largeCategoryValue) {
        System.out.println("[PART] : " + part);
        System.out.println("[categoryValue] : " + categoryValue);
        System.out.println("[largeCategoryValue] : " + largeCategoryValue);
        adminService.addCategory(part, categoryValue, largeCategoryValue);
        return "success";
    }

    @PostMapping(path = {"/modify-category"})
    @ResponseBody
    public String modifyCategory(@RequestParam(name = "categoryName") String categoryName,
                                 @RequestParam(name = "categoryId") int categoryId,
                                 @RequestParam(name = "categoryPart") String categoryPart) {
        adminService.modifyCategoryName(categoryName, categoryId, categoryPart);
        return "success";
    }

    @PostMapping(path = {"/delete-category"})
    @ResponseBody
    public String deleteCategory(@RequestParam(name = "categoryPart") String categoryPart,
                                 @RequestParam(name = "categoryId") int categoryId) {
        try {
            adminService.deleteCategory(categoryPart, categoryId);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @GetMapping(path = {"/user-manage"})
    public String userManage(Model model) {
        List<UserRoleDto> userRoles = adminService.findAllUserRole();
        model.addAttribute("userRoles", userRoles);
        return "/admin/user-manage/userManage";
    }

    @GetMapping(path = {"/get-user"})
    public String getUser(Model model, HttpServletRequest req,
                          @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                          @RequestParam(name = "category", defaultValue = "all") String category,
                          @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                          @RequestParam(name = "accordionId", defaultValue = "none") String accordionId,
                          @RequestParam(name = "sortValue", defaultValue = "score") String sortValue) {
        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = adminService.findAllUserCount(category, searchValue);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<UserDto> users = adminService.findUserList(start, category, searchValue, sortValue);

        if (users.isEmpty()) {
            return "/modules/noDataModule";
        }

        List<UserRoleDto> userRoles = adminService.findAllUserRole();
        model.addAttribute("userRoles", userRoles);

        model.addAttribute("accordionId", accordionId);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("users", users);

        return "/admin/user-manage/modules/userList";
    }

    @PostMapping(path = {"/modify-role"})
    @ResponseBody
    public String modifyRole(@RequestParam(name = "roleValue") int roleValue,
                             @RequestParam(name = "userId") String userId) {
        adminService.modifyUserRole(roleValue, userId);
        return "success";
    }

    @GetMapping(path = {"/team-manage"})
    public String teamManage() {
        return "/admin/schedule-manage/teamManage";
    }

    @GetMapping(path = {"/get-team"})
    public String getTeam(Model model, HttpServletRequest req,
                          @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                          @RequestParam(name = "category", defaultValue = "competition") String category,
                          @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int start = pageSize * (pageNo - 1);

        if (category.equals("competition")) {
            int dataCount = adminService.findAllCompetitionCount(searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

            List<CompetitionDto> competitions = adminService.findCompetition(start, searchValue);

            if (competitions.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);
            model.addAttribute("competitions", competitions);
        }
        if (category.equals("round")) {
            int dataCount = adminService.findAllCompetitionRoundCount(searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

            List<CompetitionRoundDto> rounds = adminService.findCompetitionRound(start, searchValue);

            if (rounds.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);

            model.addAttribute("rounds", rounds);
        }
        if (category.equals("team")) {
            int dataCount = adminService.findAllTeamCount(searchValue);
            String uri = req.getRequestURI();
            String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
            String queryString = req.getQueryString();

            ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

            List<TeamDto> teams = adminService.findTeam(start, searchValue);

            if (teams.isEmpty()) {
                return "/modules/noDataModule";
            }

            model.addAttribute("pager", pager);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("dataCount", dataCount);

            model.addAttribute("teams", teams);
        }

        model.addAttribute("category", category);

        return "/admin/schedule-manage/modules/teamList";
    }

    @GetMapping(path = {"/schedule-manage"})
    public String scheduleManage(Model model) {
        List<TeamDto> teams = adminService.findAllTeam();
        List<CompetitionDto> competitions = adminService.findAllCompetition();
        List<CompetitionRoundDto> rounds = adminService.findAllCompetitionRound();

        model.addAttribute("teams", teams);
        model.addAttribute("competitions", competitions);
        model.addAttribute("rounds", rounds);
        return "/admin/schedule-manage/scheduleManage";
    }

    @PostMapping(path = {"/save-schedule-category"})
    @ResponseBody
    public String saveScheduleCategory(@RequestParam(name = "value") String value,
                                       @RequestParam(name = "competitionCategory", required = false) String competitionCategory,
                                       @RequestParam(name = "logo", required = false) String logo,
                                       @RequestParam(name = "from") String from) {
        if (value.isEmpty()) {
            return "valueEmpty";
        }
        if (from.equals("team") && logo.isEmpty()) {
            return "logoEmpty";
        }
        try {
            adminService.addScheduleCategory(value, logo, from, competitionCategory);
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    @PostMapping(path = {"/save-schedule"})
    @ResponseBody
    public String saveSchedule(@RequestBody ScheduleDto schedule) {
        adminService.addSchedule(schedule);
        return "success";
    }

    @PostMapping(path = {"/modify-team"})
    @ResponseBody
    public String modifyTeam(@RequestParam(name = "id") int id,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "logo") String logo,
                             @RequestParam(name = "category") String category,
                             @RequestParam(name = "competitionCategory") String competitionCategory) {

        try {
            adminService.modifyNameAndLogo(id, name, logo, category, competitionCategory);
        } catch (Exception e) {
            return "fail";
        }

        return "success";
    }

    @PostMapping(path = {"/delete-team"})
    @ResponseBody
    public String deleteTeam(@RequestParam(name = "id") int id,
                             @RequestParam(name = "category") String category) {
        try {
            adminService.deleteTeam(id, category);
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    @GetMapping(path = {"/get-schedule-list"})
    public String getScheduleList(Model model, HttpServletRequest req,
                                  @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(name = "category", defaultValue = "all") String category,
                                  @RequestParam(name = "year") int year,
                                  @RequestParam(name = "month") int month,
                                  @RequestParam(name = "day") int day) {

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = adminService.findAllScheduleCount(category, year, month, day);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        Map<Integer, List<ScheduleDto>> schedules = adminService.findSchedule(start, category, year, month, day);

        if (schedules.isEmpty()) {
            return "/modules/noDataModule";
        }

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("schedules", schedules);

        return "/admin/schedule-manage/modules/scheduleList";
    }

    @PostMapping(path = {"/modify-schedule"})
    @ResponseBody
    public String modifySchedule(@RequestBody ScheduleRequestDto requestDto) {

        ScheduleDto schedule = requestDto.getSchedule();
        int year = requestDto.getYear();
        int month = requestDto.getMonth();
        int day = requestDto.getDay();
        int hour = requestDto.getHour();
        int minute = requestDto.getMinute();

        adminService.modifySchedule(schedule, year, month, day, hour, minute);
        return "success";
    }

    @PostMapping(path = {"/remove-schedule"})
    @ResponseBody
    public String removeSchedule(@RequestParam(name = "scheduleId") int scheduleId) {
        adminService.removeSchedule(scheduleId);
        return "success";
    }

    @GetMapping(path = {"/ranking-manage"})
    public String rankingManage(Model model) {
        List<CompetitionDto> competitions = adminService.findAllCompetition();
        List<TeamDto> teams = adminService.findAllTeam();

        model.addAttribute("competitions", competitions);
        model.addAttribute("teams", teams);
        return "/admin/ranking-manage/rankingManage";
    }

    @PostMapping(path = {"/save-ranking"})
    @ResponseBody
    public String saveRanking(@RequestParam(name = "seasonValue") int seasonValue,
                              @RequestParam(name = "competitionId") int competitionId,
                              @RequestParam(name = "homeId") int homeId,
                              @RequestParam(name = "homeScore") int homeScore,
                              @RequestParam(name = "awayId") int awayId,
                              @RequestParam(name = "awayScore") int awayScore) {

        adminService.addRanking(seasonValue, competitionId, homeId, homeScore, awayId, awayScore);

        return "success";
    }

    @PostMapping(path = {"/save-competition-league"})
    @ResponseBody
    public String saveCompetitionLeague(@RequestParam(name = "competitionId") int competitionId,
                                        @RequestParam(name = "clFrom") int clFrom,
                                        @RequestParam(name = "clTo") int clTo,
                                        @RequestParam(name = "olFrom") int olFrom,
                                        @RequestParam(name = "olTo") int olTo) {
        if (clFrom > clTo) {
            return  "clTo";
        }
        if (olFrom > olTo) {
            return "olTo";
        }
        if (clTo >= olFrom) {
            return "dup";
        }

        adminService.addCompetitionLeague(competitionId, clFrom, clTo, olFrom, olTo);

        return "success";
    }

}
