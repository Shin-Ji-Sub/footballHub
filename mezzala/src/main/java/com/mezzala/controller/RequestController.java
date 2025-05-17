package com.mezzala.controller;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.RequestBoardDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.NormalhubService;
import com.mezzala.service.RequestService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = {"request"})
public class RequestController {

    @Setter(onMethod_ = {@Autowired})
    private RequestService requestService;

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

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("category", category);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("searchValue", searchValue);
        return "request/requestHome";
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

        return "redirect:/request/home";
    }

    @GetMapping(path = {"/get-contents"})
    public String getContents(Model model, HttpServletRequest req, HttpSession session,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "category", defaultValue = "all") String category,
                              @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                              @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue) {
        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = requestService.findAllRequestBoardCount(searchValue, category);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

        List<BoardDto> boards = requestService.findRequestBoardWithPaging(start, category, searchValue, sortValue);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("boards", boards);

        return "request/modules/requestList";
    }

    @GetMapping(path = {"/write"})
    public String write() {
        return "request/write";
    }

    @PostMapping(path = {"/write-board"})
    @ResponseBody
    public String writeBoard(RequestBoardDto board) {
        requestService.addBoard(board);

        return "success";
    }

    @GetMapping(path = {"/detail-content"})
    public String detailContent(Model model, HttpServletResponse res, HttpSession session,
                                @RequestParam(name = "index") int index,
                                @RequestParam(name = "pageNo") int pageNo,
                                @RequestParam(name = "from", defaultValue = "none") String from,
                                @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                                @RequestParam(name = "category", defaultValue = "all") String category,
                                @RequestParam(name = "fromPage") String fromPage,
                                @RequestParam(name = "count") int count,
                                @RequestParam(name = "searchValue") String searchValue) {

        int boardNo = (pageNo - 1) * 10 + index;

        System.out.println("CATEGORY : " + category);

        List<RequestBoardDto> boards = requestService.findRequestBoard(boardNo, sortValue, category, searchValue);
        RequestBoardDto board = boards.get(0);

        model.addAttribute("from", from);
        model.addAttribute("index", index);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("category", category);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("fromPage", fromPage);
        model.addAttribute("count", count);
        model.addAttribute("searchValue", searchValue);

        model.addAttribute("board", board);

        return "request/content";
    }

    @PostMapping(path = {"/write-comment"})
    @ResponseBody
    public String writeComment(@RequestParam(name = "content") String content,
                               @RequestParam(name = "boardId") int boardId,
                               @RequestParam(name = "userId") String userId,
                               @RequestParam(name = "parentId", required = false) Integer parentId) {
        if (parentId == null) {
            requestService.addComment(content, boardId, userId);
        } else {
            requestService.addRecomment(content, boardId, userId, parentId);
        }

        return "success";
    }

    @GetMapping(path = {"/bring-comment"})
    public String bringComment(Model model, HttpSession session, HttpServletRequest req,
                               @RequestParam(name = "boardId") int boardId,
                               @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = requestService.findAllRequestCommentCount(boardId);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

        List<CommentDto> comments = requestService.findCommentsWithBoardId(boardId, start);

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("comments", comments);

        return "request/modules/commentModule";
    }

    @PostMapping(path = {"/delete-comment"})
    @ResponseBody
    public String deleteComment(@RequestParam(name = "commentId") int commentId,
                                @RequestParam(name = "boardId") int boardId) {
        requestService.deleteCommentWithCommentIdAndBoardId(commentId, boardId);

        return "success";
    }

    @PostMapping(path = {"/modify-comment"})
    @ResponseBody
    public String modifyComment(@RequestParam(name = "commentId") int commentId,
                                @RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "content") String content) {
        requestService.modifyCommentWithCommentIdAndBoardId(commentId, boardId, content);

        return "success";
    }

}
