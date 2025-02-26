package com.mezzala.controller;

import com.mezzala.common.Util;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.service.AccountService;
import com.mezzala.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
import java.util.UUID;

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

        return "write";
    }

    /**
     * 에디터 이미지 업로드
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping(path = {"/image-upload"})
    @ResponseBody
    public String uploadEditorImage(@RequestParam final MultipartFile image, HttpServletRequest req) {
        if (image.isEmpty()) {
            return "";
        }

        try {
            String dir = req.getServletContext().getRealPath("/board-attachments");
            String userFileName = image.getOriginalFilename();
            String savedFileName = Util.makeUniqueFileName(userFileName);
            image.transferTo(new File(dir, savedFileName));
            return savedFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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

}
