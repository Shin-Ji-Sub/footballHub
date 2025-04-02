package com.mezzala.controller;

import com.mezzala.common.KakaoApi;
import com.mezzala.common.NaverApi;
import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.AccountService;
import com.mezzala.service.NormalhubService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = {"normalhub"})
public class NormalhubController {

    @Setter(onMethod_ = {@Autowired})
    private NormalhubService normalhubService;

    @GetMapping(path = {"/home"})
    public String home(Model model) {

        List<BoardSmallCategoryDto> smallCategories = normalhubService.findAllBoardSmallCategory();

        model.addAttribute("smallCategories", smallCategories);

        return "/normalhub/normalhubHome";
    }

    @GetMapping(path = {"/get-contents"})
    public String getContents(Model model, HttpServletRequest req, HttpSession session,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                              @RequestParam(name = "category", defaultValue = "all") String category,
                              @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {
        UserDto user = (UserDto) session.getAttribute("user");
        String userId = "";
        if (user != null) {
            userId = user.getUserId();
        }

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = normalhubService.findAllBoardCount(userId);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = normalhubService.findBoardWithPaging(start, category, searchValue, userId, sortValue);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "/normalhub/modules/content";
    }

}
