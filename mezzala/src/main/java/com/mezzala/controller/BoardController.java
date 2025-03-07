package com.mezzala.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mezzala.common.Util;
import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.UserActionDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.AccountService;
import com.mezzala.service.BoardService;
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
@RequestMapping(path = {"board"})
public class BoardController {

    @Setter(onMethod_ = {@Autowired})
    private BoardService boardService;

    // 파일을 업로드할 디렉터리 경로
//    private final String uploadDir = "/webapp/board-attachments";
//    private final String uploadDir = Paths.get("D:", "tui-editor", "upload").toString();

    @GetMapping(path = {"/write"})
    public String write(Model model) {

        List<BoardLargeCategoryDto> largeCategories = boardService.findAllCategory();

        model.addAttribute("largeCategories", largeCategories);

        return "/board/write";
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

        try {
            String dir = req.getServletContext().getRealPath("/board-attachments");
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
        String dir = req.getServletContext().getRealPath("/board-attachments");
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
        System.out.println("board : " + board);
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

        boardService.addBoard(board, imageFiles);

        return "success";
    }

    private static class UploadResponse {
        public boolean success;
        public String url;

        public UploadResponse(boolean success, String url) {
            this.success = success;
            this.url = url;
        }
    }

    @PostMapping(path = "/upload-video", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UploadResponse uploadVideo(@RequestParam("video") MultipartFile file, HttpServletRequest req) {
        try {
            // 업로드 주소
            String dir = req.getServletContext().getRealPath("/board-attachments");

            // 1. 확장자 체크
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

            System.out.println("📂 업로드된 파일: " + originalFilename);
            System.out.println("📏 파일 크기: " + file.getSize() + " bytes");

            if (!fileExtension.matches("mp4|webm|mov")) {
                return new UploadResponse(false, "Invalid file type");
            }

            // 2. 크기 체크 (20MB 이하)
            if (file.getSize() > 20 * 1024 * 1024) {
                return new UploadResponse(false, "File too large");
            }

            // 3. 저장할 파일명 생성 (UUID 사용)
            String savedFileName = UUID.randomUUID() + "." + fileExtension;
            file.transferTo(new File(dir, savedFileName));

            // 4. 저장된 파일의 URL 반환
            String fileUrl = "/board-attachments/" + savedFileName;
            System.out.println("✅ 저장된 파일 URL: " + fileUrl);

            return new UploadResponse(true, fileUrl);
//            String fileUrl = req.getContextPath() + "/board-attachments/" + savedFileName;
//            return new UploadResponse(true, fileUrl);


        } catch (IOException e) {
            e.printStackTrace();
            return new UploadResponse(false, "Upload failed");
        }
    }

//    @PostMapping(path = {"/video-upload"})
//    @ResponseBody
//    public String uploadEditorVideo(@RequestParam final MultipartFile file, HttpServletRequest req) {
//        if (file.isEmpty()) {
//            return "";
//        }
//
//        try {
//            String dir = req.getServletContext().getRealPath("/board-attachments");
//            String userFileName = file.getOriginalFilename();
//            String savedFileName = Util.makeUniqueFileName(userFileName);
//            file.transferTo(new File(dir, savedFileName));
//            return savedFileName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    @GetMapping(path = {"/video-print"}, produces = {MediaType.})
//    @ResponseBody
//    public byte[] printEditorVideo(@RequestParam final String filename, HttpServletRequest req) {
//        // 업로드된 파일의 전체 경로
//        String dir = req.getServletContext().getRealPath("/board-attachments");
//        String fileFullPath = Paths.get(dir, filename).toString();
//        File uploadedFile = new File(fileFullPath);
//        if (uploadedFile.exists() == false) {
//            throw new RuntimeException();
//        }
//        try {
//            // 이미지 파일을 byte[]로 변환 후 반환
//            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
//            return imageBytes;
//        } catch (Exception e) {
//            e.printStackTrace();
//            byte[] error = {1};
//            return error;
//        }
//    }

    @GetMapping(path = {"content"})
    public String content(Model model, @RequestParam(name = "boardId") int boardId,
                          @RequestParam(name = "index") int index,
                          @RequestParam(name = "count") int count,
                          @RequestParam(name = "pageNo") int pageNo,
                          @CookieValue(value = "visited", required = false) String visitedBoard,
                          HttpServletResponse res,
                          HttpSession session) {
        // 쿠키에 현재 boardId가 포함되어 있는지 확인
        if (visitedBoard == null || !visitedBoard.contains("[" + boardId + "]")) {
            boardService.incrementVisitedBoard(boardId); // 조회수 증가

            // 쿠키에 현재 boardId 추가
            visitedBoard = (visitedBoard == null ? "" : visitedBoard) + "[" + boardId + "]";
            Cookie cookie = new Cookie("visited", visitedBoard);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간(1일)
            res.addCookie(cookie);
        }

        List<BoardDto> boards = boardService.findBoardWithBoardId(boardId);
        UserDto user = (UserDto) session.getAttribute("user");
        List<UserActionDto> actions = new ArrayList<>();
        if (user == null) {
            UserActionDto action = new UserActionDto();
            actions.add(action);
        } else {
            actions = user.getUserActions();
        }

        model.addAttribute("actions", actions);
        model.addAttribute("board", boards.get(0));
        model.addAttribute("index", index);
        model.addAttribute("count", count);
        model.addAttribute("pageNo", pageNo);

        return "/board/content";
    }

    @GetMapping(path = {"contentToContent"})
    public String contentToContent(Model model, HttpServletResponse res,
                                   @CookieValue(value = "visited", required = false) String visitedBoard,
                                   @RequestParam(name = "boardNo") int boardNo,
                                   @RequestParam(name = "index") int index,
                                   @RequestParam(name = "pageNo") int pageNo,
                                   @RequestParam(name = "count") int count) {
        List<BoardDto> boards = boardService.findBoardWithBoardNo(boardNo);
        BoardDto board = boards.get(0);

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

        System.out.println(board);

        model.addAttribute("board", board);
        model.addAttribute("index", index);
        model.addAttribute("count", count);
        model.addAttribute("pageNo", pageNo);
        return "/board/content";
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

        return result;
    }

    @GetMapping(path = {"/action-button"})
    public String actionButton(Model model, @RequestParam(name = "likeAction") String likeAction) {
        model.addAttribute("likeAction", likeAction);
        return "/board/modules/userActionModule";
    }

    @GetMapping(path = {"/action-like"})
    public String likeAction(@RequestParam(name = "boardId") int boardId,
                             Model model) {
        List<BoardDto> boards = boardService.findBoardWithBoardId(boardId);
        model.addAttribute("board", boards.get(0));
        return "/board/modules/likeModule";
    }

}
