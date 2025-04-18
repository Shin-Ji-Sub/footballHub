package com.mezzala.controller;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.BoardService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Setter(onMethod_ = {@Autowired})
    private BoardService boardService;

    @RequestMapping(path = {"/", "/home"})
    public String home(Model model,
                       @RequestParam(name = "noticePageNo", defaultValue = "1") int noticePageNo,
                       @RequestParam(name = "mainPageNo", defaultValue = "1") int mainPageNo,
                       @RequestParam(name = "sortValue", defaultValue = "bestList") String sortValue,
                       @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {

        model.addAttribute("noticePageNo", noticePageNo);
        model.addAttribute("mainPageNo", mainPageNo);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("searchValue", searchValue);

        return "home";
    }

    @GetMapping(path = {"/content-list"})
    public String contentList(Model model, HttpServletRequest req, HttpSession session,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "sortValue", defaultValue = "bestList") String sortValue,
                              @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {
        UserDto user = (UserDto) session.getAttribute("user");
        String userId = "";
        if (user != null) {
            userId = user.getUserId();
        }

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = boardService.findAllBoardCount(userId, searchValue);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

        List<BoardDto> boards = boardService.findBoardWithPaging(start, sortValue, searchValue, userId);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("sortValue", sortValue);
        model.addAttribute("searchValue", searchValue);

        model.addAttribute("boards", boards);

        return "/modules/home-pagination";
    }

    @GetMapping(path = {"/notice-list"})
    public String noticeList(Model model, HttpServletRequest req,
                             @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        // paging
        int pageSize = 5;
        int pagerSize = 5;
        int dataCount = boardService.findAllNoticeBoardCount();
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = boardService.findNoticeBoardWithPaging(start);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "/modules/homeNoticeList";
    }

}
