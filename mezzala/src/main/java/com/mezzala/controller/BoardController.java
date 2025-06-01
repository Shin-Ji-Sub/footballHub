package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.common.Util;
import com.mezzala.dto.*;
import com.mezzala.service.AccountService;
import com.mezzala.service.BoardService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping(path = {"board"})
public class BoardController {

    @Setter(onMethod_ = {@Autowired})
    private BoardService boardService;

    @Setter(onMethod_ = {@Autowired})
    private AccountService accountService;

    // 파일을 업로드할 디렉터리 경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping(path = {"/write"})
    public String write(Model model,
                        @RequestParam(name = "fromPage", defaultValue = "home") String fromPage) {

        List<BoardLargeCategoryDto> largeCategories = boardService.findAllCategory();
        String categoryText = "";
        if (fromPage.equals("normalhub")) {
            categoryText = "일반";
        } else if (fromPage.equals("fandomhub")) {
            categoryText = "공화국";
        }

        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("fromPage", fromPage);
        model.addAttribute("categoryText", categoryText);

        return "board/write";
    }

    @GetMapping(path = {"/get-small-category"})
    public String getSmallCategory(Model model,
                                   @RequestParam(name = "largeCategory", defaultValue = "0") int largeCategory) {
        List<BoardSmallCategoryDto> smallCategories = null;
        if (largeCategory != 0) {
            smallCategories = boardService.findSmallCategoryWithLargeCategoryId(largeCategory);
        }
        model.addAttribute("smallCategories", smallCategories);
        return "board/modules/smallCategorySelectModule";
    }

    @GetMapping(path = {"/get-modify-small-category"})
    public String getModifySmallCategory(Model model,
                                         @RequestParam(name = "largeCategory", defaultValue = "0") int largeCategory,
                                         @RequestParam(name = "boardSmallCategory") int boardSmallCategory) {
        List<BoardSmallCategoryDto> smallCategories = null;
        if (largeCategory != 0) {
            smallCategories = boardService.findSmallCategoryWithLargeCategoryId(largeCategory);
        }
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("boardSmallCategory", boardSmallCategory);
        return "board/modules/modifySmallCategorySelectModule";
    }

    /**
     * 에디터 이미지 업로드
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping(path = {"/image-upload"})
    @ResponseBody
    public Map<String, String> uploadEditorImage(@RequestParam final MultipartFile image, HttpServletRequest req, Model model) {
        Map<String, String> result = new HashMap<>();

        if (image.isEmpty()) {
            return result;
        }
        System.out.println(image.getSize());
        // 🔐 파일 사이즈 체크 (20MB = 20 * 1024 * 1024)
        if (image.getSize() > 10 * 1024 * 1024) {
            result.put("error", "파일 크기는 20MB를 초과할 수 없습니다.");
            return result;
        }

        try {
            String dir = uploadDir;
//            String dir = req.getServletContext().getRealPath("/board-attachments");
            String userFileName = image.getOriginalFilename();
            String savedFileName = Util.makeUniqueFileName(userFileName);
            image.transferTo(new File(dir, savedFileName));

            result.put("userFileName", userFileName);
            result.put("savedFileName", savedFileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

//        String orgFilename = image.getOriginalFilename();                                          // 원본 파일명
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");            // 32자리 랜덤 문자열
//        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);   // 확장자
//        String saveFilename = uuid + "." + extension;                                              // 디스크에 저장할 파일명
//        String fileFullPath = Paths.get(uploadDir, saveFilename).toString();                       // 디스크에 저장할 파일의 전체 경로



        // uploadDir에 해당되는 디렉터리가 없으면, uploadDir에 포함되는 전체 디렉터리 생성
//        File dir = new File(uploadDir);
//        if (dir.exists() == false) {
//            dir.mkdirs();
//        }
//
//        try {
//            // 파일 저장 (write to disk)
//            File uploadFile = new File(fileFullPath);
//            image.transferTo(uploadFile);
//            return saveFilename;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
    }

    /**
     * 디스크에 업로드된 파일을 byte[] 로 변환
     *
     * @param filename 디스크에 업로드된 파일명
     * @return image byte array
     */
    @GetMapping(path = {"/image-print"}, produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] printEditorImage(@RequestParam final String filename, HttpServletRequest req) {
        // 업로드된 파일의 전체 경로
        String dir = uploadDir;
//        String dir = req.getServletContext().getRealPath("/board-attachments");
        String fileFullPath = Paths.get(dir, filename).toString();
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }
        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            byte[] error = {1};
            return error;
        }

//        // 업로드된 파일의 전체 경로
//        String fileFullPath = Paths.get(uploadDir, filename).toString();
//
//        // 파일이 없는 경우 예외 throw
//        File uploadedFile = new File(fileFullPath);
//        if (uploadedFile.exists() == false) {
//            throw new RuntimeException();
//        }
//
//        try {
//            // 이미지 파일을 byte[]로 변환 후 반환
//            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
//            return imageBytes;
//        } catch (Exception e) {
//            e.printStackTrace();
//            byte[] error = {1};
//            return error;
//        }
    }

    @PostMapping(path = {"/write-board"})
    @ResponseBody
    public String writeBoard(BoardDto board, @RequestParam(name = "ImageFilesArr", required = false) String ImageFilesArr) {

        List<Map<String, String>> imageFiles = new ArrayList<>();
        if (ImageFilesArr != null && !ImageFilesArr.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                imageFiles = objectMapper.readValue(ImageFilesArr, new TypeReference<List<Map<String, String>>>() {});

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        boardService.addBoard(board, imageFiles);

        return "success";
    }

    @PostMapping(path = {"/modify-board"})
    @ResponseBody
    public String modifyBoard(BoardDto board, @RequestParam(name = "ImageFilesArr", required = false) String ImageFilesArr) {

        List<Map<String, String>> imageFiles = new ArrayList<>();
        if (ImageFilesArr != null && !ImageFilesArr.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                imageFiles = objectMapper.readValue(ImageFilesArr, new TypeReference<List<Map<String, String>>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        boardService.modifyBoard(board, imageFiles);

        return "success";
    }

    @GetMapping(path = {"content"})
    public String content(Model model, HttpServletResponse res, HttpSession session,
                          @RequestParam(name = "index") int index,
                          @RequestParam(name = "pageNo") int pageNo,
                          @RequestParam(name = "from") String from,
                          @RequestParam(name = "count") int count,
                          @RequestParam(name = "sortValue", required = false) String sortValue,
                          @RequestParam(name = "searchValue", required = false) String searchValue,
                          @RequestParam(name = "tabNo", required = false) Integer tabNo,
                          @CookieValue(value = "visited", required = false) String visitedBoard) {

        int boardNo = (pageNo - 1) * 10 + index;

        UserDto user = (UserDto) session.getAttribute("user");
        String userId = "";
        if (user != null) {
            userId = user.getUserId();
        }

        BoardDto board = null;
        if (from.equals("mypage")) {
            List<BoardDto> boards = boardService.findMypageBoard(boardNo, userId, tabNo);
            board = boards.get(0);
        } else {
            List<BoardDto> boards = boardService.findBoardWithBoardNo(boardNo, userId, sortValue, searchValue);
            board = boards.get(0);
        }

        List<UserActionDto> likeActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            likeActions.add(action);
        } else {
            likeActions = user.getLikeUserActions();
        }
        List<UserActionDto> bookmarkActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            bookmarkActions.add(action);
        } else {
            bookmarkActions = user.getBookmarkUserActions();
        }

        // 쿠키에 현재 boardId가 포함되어 있는지 확인
        if (visitedBoard == null || !visitedBoard.contains("[" + board.getBoardId() + "]")) {
            boardService.incrementVisitedBoard(board.getBoardId()); // 조회수 증가

            // 쿠키에 현재 boardId 추가
            visitedBoard = (visitedBoard == null ? "" : visitedBoard) + "[" + board.getBoardId() + "]";
            Cookie cookie = new Cookie("visited", visitedBoard);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간(1일)
            res.addCookie(cookie);
        }

        System.out.println("[content user] : " + user);

        model.addAttribute("likeActions", likeActions);
        model.addAttribute("bookmarkActions", bookmarkActions);
        model.addAttribute("board", board);

        model.addAttribute("index", index);
        model.addAttribute("count", count);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("from", from);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("searchValue", searchValue);
        if (tabNo != null) {
            model.addAttribute("tabNo", tabNo);
        }

        return "board/content";
    }

    @GetMapping(path = {"/modify-content"})
    public String modifyContent(Model model,
                                @RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "returnUri") String returnUri) {

        List<BoardDto> boards = boardService.findBoardWithBoardId(boardId);
        List<BoardLargeCategoryDto> largeCategories = boardService.findAllCategory();

        model.addAttribute("board", boards.get(0));
        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("returnUri", returnUri);

        return "board/modify";
    }

    @PostMapping(path = {"/before-delete"})
    @ResponseBody
    public String beforeDelete(@RequestParam(name = "writeUserId") String writeUserId,
                               HttpSession session) {
        // Request 페이지에서 사용(주석처리했음)
        UserDto user = (UserDto) session.getAttribute("user");
        if (writeUserId.equals(user.getUserId())) {
            return "success";
        } else {
            return "fail";
        }

    }

    @GetMapping(path = {"/delete-content"})
    public String deleteContent(@RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "returnUri", defaultValue = "/") String returnUri) {

        if (returnUri.equals("/request/home")) {
            boardService.deleteRequestBaord(boardId);
        } else {
            boardService.deleteContent(boardId);
        }

        if (returnUri.equals("/")) {
            return "redirect:/";
        } else {
            return "redirect:" + returnUri;
        }
    }

    @PostMapping(path = {"/action"})
    @ResponseBody
    public String action(@RequestParam(name = "actionCategory") String actionCategory,
                         @RequestParam(name = "boardId") int boardId,
                         @RequestParam(name = "state") String state,
                         HttpSession session) {
        String result = "";
        UserDto user = (UserDto) session.getAttribute("user");

        if (user == null) {
            result = "login";
        } else {
            if (state.equals("add")) {
                boardService.addUserAction(user, boardId, actionCategory);
                result = "success";
            }
            if (state.equals("remove")) {
                boardService.removeUserAction(user, boardId, actionCategory);
                result = "success";
            }
        }

        // session user 정보 업데이트
        List<UserDto> updateUsers = accountService.findUser(user.getUserId());
        UserDto updateUser = updateUsers.get(0);
        session.setAttribute("user", updateUser);

        return result;
    }

    @GetMapping(path = {"/action-button"})
    public String actionButton(Model model,
                               @RequestParam(name = "likeAction") String likeAction,
                               @RequestParam(name = "bookmarkAction") String bookmarkAction) {
        model.addAttribute("likeAction", likeAction);
        model.addAttribute("bookmarkAction", bookmarkAction);
        return "board/modules/userActionModule";
    }

    @GetMapping(path = {"/action-like"})
    public String actionLike(@RequestParam(name = "boardId") int boardId,
                             Model model) {
        List<BoardDto> boards = boardService.findBoardWithBoardId(boardId);
        model.addAttribute("board", boards.get(0));
        return "board/modules/likeModule";
    }

    @GetMapping(path = {"/action-bookmark"})
    public String actionBookmark(@RequestParam("userId") String userId,
                                 @RequestParam("category") String category) {


        return "success";
    }

    @PostMapping(path = {"/write-comment"})
    @ResponseBody
    public String writeComment(@RequestParam(name = "content") String content,
                               @RequestParam(name = "boardId") int boardId,
                               @RequestParam(name = "parentId", required = false) Integer parentId,
                               HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");
        String userId = user.getUserId();

        if (parentId == null) {
            boardService.addComment(content, boardId, userId);
        } else {
            boardService.addRecomment(content, boardId, userId, parentId);
        }

        return "success";
    }

    @GetMapping(path = {"/bring-comment"})
    public String bringComment(Model model, HttpSession session, HttpServletRequest req,
                               @RequestParam(name = "boardId") int boardId,
                               @RequestParam(name = "pageNo", required = false) Integer pageNo) {

        UserDto user = (UserDto) session.getAttribute("user");
        String userId = user != null ? user.getUserId() : "";

        pageNo = (pageNo != null) ? pageNo : 1;

        // paging
        int pageSize = 10;
        int pagerSize = 5;
        int dataCount = boardService.findAllCommentCount(boardId, userId);
        String uri = req.getRequestURI();
        String linkUrl = uri.substring(uri.lastIndexOf("/") + 1);
        String queryString = req.getQueryString();

        int start = pageSize * (pageNo - 1);

        ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize, linkUrl, queryString);

        if (user != null) {
            List<CommentDto> commentActions = boardService.findCommentActions(boardId, user.getUserId());
            model.addAttribute("commentActions", commentActions);
        }

        List<CommentDto> comments = boardService.findCommentsWithBoardId(boardId, start, userId);

        int best = -1;
        int bestCommentId = -1;
        for (CommentDto c : comments) {
            if (best < c.getCommentActions().size() && c.getCommentActions().size() > 10) {
                best = c.getCommentActions().size();
                bestCommentId = c.getCommentId();
            }
        }

        model.addAttribute("pager", pager);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("dataCount", dataCount);

        model.addAttribute("bestCommentId", bestCommentId);
        model.addAttribute("comments", comments);

        return "board/modules/commentModule";
    }

    @PostMapping(path = {"/delete-comment"})
    @ResponseBody
    public String deleteComment(@RequestParam(name = "commentId") int commentId,
                                @RequestParam(name = "boardId") int boardId) {
        boardService.deleteCommentWithCommentIdAndBoardId(commentId, boardId);

        return "success";
    }

    @PostMapping(path = {"/modify-comment"})
    @ResponseBody
    public String modifyComment(@RequestParam(name = "commentId") int commentId,
                                @RequestParam(name = "boardId") int boardId,
                                @RequestParam(name = "content") String content) {

        boardService.modifyCommentWithCommentIdAndBoardId(commentId, boardId, content);

        return "success";
    }

    @PostMapping(path = {"/recommendation-comment"})
    @ResponseBody
    public String recommendationComment(@RequestParam(name = "commentId") int commentId,
                                        HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");
        boardService.addCommentAction(commentId, user.getUserId());

        return "success";
    }

    @PostMapping(path = {"/cancel-comment-recommendation"})
    @ResponseBody
    public String cancelCommentRecommendation(@RequestParam(name = "commentId") int commentId,
                                              HttpSession session) {

        UserDto user = (UserDto) session.getAttribute("user");
        boardService.deleteCommentRecommendation(commentId, user.getUserId());

        return "success";
    }

    @GetMapping(path = {"/hub-content"})
    public String hubContent(Model model, HttpServletResponse res, HttpSession session,
                             @CookieValue(value = "visited", required = false) String visitedBoard,
                             @RequestParam(name = "index") int index,
                             @RequestParam(name = "pageNo") int pageNo,
                             @RequestParam(name = "from", defaultValue = "none") String from,
                             @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                             @RequestParam(name = "category", defaultValue = "all") String category,
                             @RequestParam(name = "fromPage") String fromPage,
                             @RequestParam(name = "count") int count,
                             @RequestParam(name = "searchValue") String searchValue
                             ) {

        String userId = "";
        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            userId = user.getUserId();
        }

        int boardNo = (pageNo - 1) * 10 + index;
        int largeCategory = 0;
        if (fromPage.equals("normalhub")) {
            largeCategory = 1;
        } else if (fromPage.equals("fandomhub")) {
            largeCategory = 2;
        }

        List<BoardDto> boards = boardService.findHubBoard(boardNo, sortValue, category, largeCategory, userId, searchValue);
        BoardDto board = boards.get(0);
        List<UserActionDto> likeActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            likeActions.add(action);
        } else {
            likeActions = user.getLikeUserActions();
        }
        List<UserActionDto> bookmarkActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            bookmarkActions.add(action);
        } else {
            bookmarkActions = user.getBookmarkUserActions();
        }

        // 쿠키에 현재 boardId가 포함되어 있는지 확인
        if (visitedBoard == null || !visitedBoard.contains("[" + board.getBoardId() + "]")) {
            boardService.incrementVisitedBoard(board.getBoardId()); // 조회수 증가

            // 쿠키에 현재 boardId 추가
            visitedBoard = (visitedBoard == null ? "" : visitedBoard) + "[" + board.getBoardId() + "]";
            Cookie cookie = new Cookie("visited", visitedBoard);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간(1일)
            res.addCookie(cookie);
        }

        model.addAttribute("from", from);
        model.addAttribute("index", index);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("category", category);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("fromPage", fromPage);
        model.addAttribute("count", count);
        model.addAttribute("searchValue", searchValue);

        model.addAttribute("likeActions", likeActions);
        model.addAttribute("bookmarkActions", bookmarkActions);

        model.addAttribute("board", board);

//        return "/" + fromPage + "/modules/content";
        return fromPage + "/modules/content";
    }

    @GetMapping(path = {"/notice-content"})
    public String noticeContent(Model model, HttpServletResponse res, HttpSession session,
                             @CookieValue(value = "visited", required = false) String visitedBoard,
                             @RequestParam(name = "index") int index,
                             @RequestParam(name = "count") int count,
                             @RequestParam(name = "pageNo") int pageNo,
                             @RequestParam(name = "fromPage") String fromPage,
//                             @RequestParam(name = "from", defaultValue = "none") String from,
//                             @RequestParam(name = "category", defaultValue = "all") String category,
                             @RequestParam(name = "sortValue", defaultValue = "latest") String sortValue,
                             @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {

        String userId = "";
        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            userId = user.getUserId();
        }

        int boardNo = 0;
        if (fromPage.equals("home")) {
            boardNo = (pageNo - 1) * 5 + index;
        }
        List<BoardDto> boards = boardService.findNoticeBoard(boardNo, fromPage);
        BoardDto board = boards.get(0);

        List<UserActionDto> likeActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            likeActions.add(action);
        } else {
            likeActions = user.getLikeUserActions();
        }
        List<UserActionDto> bookmarkActions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            bookmarkActions.add(action);
        } else {
            bookmarkActions = user.getBookmarkUserActions();
        }

        // 쿠키에 현재 boardId가 포함되어 있는지 확인
        if (visitedBoard == null || !visitedBoard.contains("[" + board.getBoardId() + "]")) {
            boardService.incrementVisitedBoard(board.getBoardId()); // 조회수 증가

            // 쿠키에 현재 boardId 추가
            visitedBoard = (visitedBoard == null ? "" : visitedBoard) + "[" + board.getBoardId() + "]";
            Cookie cookie = new Cookie("visited", visitedBoard);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간(1일)
            res.addCookie(cookie);
        }

        model.addAttribute("index", index);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("sortValue", sortValue);
        model.addAttribute("fromPage", fromPage);
        model.addAttribute("count", count);
        model.addAttribute("searchValue", searchValue);

        model.addAttribute("likeActions", likeActions);
        model.addAttribute("bookmarkActions", bookmarkActions);

        model.addAttribute("board", board);

        return "board/noticeContent";
    }


}
