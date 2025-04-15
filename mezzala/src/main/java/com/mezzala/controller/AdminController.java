package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.dto.BoardDto;
import com.mezzala.service.AdminService;
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
        return "/admin/notice";
    }

    @GetMapping(path = {"/write-notice"})
    public String writeNotice() {
        return "/admin/writeNotice";
    }

    @PostMapping(path = {"/write-board"})
    @ResponseBody
    public String writeBoard(BoardDto board, @RequestParam(name = "ImageFilesArr", required = false) String ImageFilesArr) {
        List<Map<String, String>> imageFiles = new ArrayList<>();
        if (ImageFilesArr != null && !ImageFilesArr.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                imageFiles = objectMapper.readValue(ImageFilesArr, new TypeReference<List<Map<String, String>>>() {});
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
        int dataCount = adminService.findAllBoardCount(searchValue, state);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = adminService.findBoardWithPaging(start, searchValue, state);
        if (boards.isEmpty()) {
            pageNo = pageNo - 1;
            start = pageSize * (pageNo - 1);
            pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
            try {
                boards = adminService.findBoardWithPaging(start, searchValue, state);
            } catch (Exception e) {
                return "/modules/noDataModule";
            }
        }

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "/admin/modules/" + from;
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
                          @RequestParam(name = "index", defaultValue = "0") int index,
                          @RequestParam(name = "count", defaultValue = "0") int count,
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

        List<BoardDto> boards = adminService.findBoardWithBoardId(boardId);
        BoardDto board = boards.get(0);

        model.addAttribute("board", board);

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("index", index);
        model.addAttribute("count", count);

        return "/admin/content";
    }

    @PostMapping(path = {"/save-notice"})
    @ResponseBody
    public String saveNotice(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> contentIds = request.get("contentIds");
        adminService.modifyBoardsState(contentIds);
        return "success";
    }

}
