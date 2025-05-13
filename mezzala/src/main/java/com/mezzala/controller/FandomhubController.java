package com.mezzala.controller;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.FandomhubService;
import com.mezzala.service.NormalhubService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = {"fandomhub"})
public class FandomhubController {

    @Setter(onMethod_ = {@Autowired})
    private FandomhubService fandomhubService;

    @GetMapping(path = {"/home"})
    public String home(Model model
//                       @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
//                       @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
//                       @RequestParam(name = "category", defaultValue = "all") String category,
//                       @RequestParam(name = "searchValue", defaultValue = "") String searchValue
    ) {

        // FlashAttribute에서 꺼냄
        Integer pageNo = (Integer) model.asMap().get("pageNo");
        String sortValue = (String) model.asMap().get("sortValue");
        String category = (String) model.asMap().get("category");
        String searchValue = (String) model.asMap().get("searchValue");

        // 기본값 처리
        if (pageNo == null) pageNo = 1;
        if (sortValue == null) sortValue = "latest";
        if (category == null) category = "all";
        if (searchValue == null) searchValue = "";

        List<BoardSmallCategoryDto> smallCategories = fandomhubService.findAllBoardSmallCategory();
        BoardSmallCategoryDto findSmallCategory = null;
        if (!category.equals("all")) {
            for (BoardSmallCategoryDto sc : smallCategories) {
                if (sc.getSmallCategoryIndex() == Integer.parseInt(category)) {
                    findSmallCategory = sc;
                }
            }
        }

        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("findSmallCategory", findSmallCategory);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("category", category);
        model.addAttribute("searchValue", searchValue);

        return "/fandomhub/fandomhubHome";
    }

    @PostMapping(path = {"/home"})
    public String postHome(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                           @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                           @RequestParam(name = "category", defaultValue = "all") String category,
                           @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                           RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("pageNo", pageNo);
        redirectAttributes.addFlashAttribute("sortValue", sortValue);
        redirectAttributes.addFlashAttribute("category", category);
        redirectAttributes.addFlashAttribute("searchValue", searchValue);

        return "redirect:/fandomhub/home";
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
        int dataCount = fandomhubService.findAllBoardCount(userId, category, searchValue);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);
        List<BoardDto> boards = fandomhubService.findBoardWithPaging(start, category, searchValue, userId, sortValue);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "/fandomhub/modules/hubList";
    }

}
