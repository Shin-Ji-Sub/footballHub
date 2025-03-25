package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.common.Util;
import com.mezzala.dto.*;
import com.mezzala.service.BoardService;
import com.mezzala.service.MypageService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping(path = {"mypage"})
public class MypageController {

    @Setter(onMethod_ = {@Autowired})
    private MypageService mypageService;

    @GetMapping(path = {"/myinfo"})
    public String myinfo() {

        return "/mypage/mypage";
    }

    @GetMapping(path = {"/get-myinfo"})
    public String myInfoModule() {

        return "/mypage/modules/myinfoModule";
    }

    @PostMapping(path = {"/modify-nickname"})
    @ResponseBody
    public String modifyNickname(HttpSession session,
                                 @RequestParam(name = "nickname") String nickname,
                                 @RequestParam(name = "userId") String userId) {

        UserDto user = mypageService.modifyUserNickname(nickname, userId);
        session.setAttribute("user", user);

        return "success";
    }

    @GetMapping(path = {"/get-written-content"})
    public String getWrittenContent(Model model, HttpServletRequest req,
                                    @RequestParam(name = "userId") String userId,
                                    @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        // paging
        int pageSize = 6;
        int pagerSize = 5;
        int dataCount = mypageService.findBoardCountWithUserId(userId);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = mypageService.findBoardWithUserId(start, userId);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);
        model.addAttribute("boards", boards);

        return "/mypage/modules/myWrittenContentModule";
    }

}
