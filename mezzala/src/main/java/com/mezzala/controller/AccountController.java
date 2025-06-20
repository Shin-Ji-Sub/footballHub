package com.mezzala.controller;

import com.mezzala.common.Aes;
import com.mezzala.common.KakaoApi;
import com.mezzala.common.NaverApi;
import com.mezzala.common.Util;
import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.AccountService;
import com.mezzala.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = {"account"})
public class AccountController {

    private final KakaoApi kakaoApi;
    private final NaverApi naverApi;
    private final Aes aes;

    @Setter(onMethod_ = {@Autowired})
    private AccountService accountService;

    @Setter(onMethod_ = {@Autowired})
    private BoardService boardService;

    @GetMapping(path = {"/sign-up"})
    public String signUp(Model model,
                         @RequestParam(name = "returnUri", defaultValue = "/home") String returnUri,
                         @RequestParam(name = "error", required = false) String error) {
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        model.addAttribute("naverApiKey", naverApi.getNaverApiKey());
        model.addAttribute("naverRedirectUri", naverApi.getNaverRedirectUri());
        model.addAttribute("returnUri", returnUri);

        Boolean isSuccess = (Boolean) model.asMap().get("isSuccess");
        // 기본값 처리
        if (isSuccess == null) isSuccess = true;

        model.addAttribute("isSuccess", isSuccess);
        model.addAttribute("error", error);

        return "account/sign-up";
    }

//    @RequestMapping(path = {"/kakao/api"})
//    public String kakaoSignIn(@RequestParam String code,
//                              @RequestParam String state,
//                              HttpSession session, RedirectAttributes redirectAttributes) {
//        String accessToken = kakaoApi.getAccessToken(code);
//        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);
//
//        String userId = (String) userInfo.get("userId");
//        String nickname = (String) userInfo.get("nickname");
//        String socialMethod = "kakao";
//
////        System.out.println("nickname : " + nickname);
////        System.out.println("userId : " + userId);
////        System.out.println("accessToken : " + accessToken);
//
//        try {
//            List<UserDto> users = accountService.addAccount(userId, nickname, socialMethod);
//            session.setAttribute("accessToken", accessToken);
//            session.setAttribute("user", users.get(0));
//        } catch (Exception e) {
//            boolean isSuccess = false;
//            redirectAttributes.addFlashAttribute("isSuccess", isSuccess);
//            return "redirect:/account/sign-up";
////            e.printStackTrace();
//        }
//
//        state = URLDecoder.decode(state, StandardCharsets.UTF_8);
//        return "redirect:" + state;
//    }

    @GetMapping(path = {"/log-out"})
    public String logOut(HttpSession session, @RequestParam(name="socialMethod") String socialMethod,
                         @RequestParam(name = "returnUri", defaultValue = "/home") String returnUri) throws UnsupportedEncodingException {

//        String accessToken = (String) session.getAttribute("accessToken");
        if (socialMethod.equals("kakao")) {
            HashMap<String, String> kakao = kakaoApi.kakaoLogout();
            String clientId = kakao.get("clientId");
            String logoutUri = kakao.get("logoutUri");

            returnUri = URLEncoder.encode(returnUri, StandardCharsets.UTF_8.toString());

            return "redirect:https://kauth.kakao.com/oauth/logout?client_id=" + clientId + "&logout_redirect_uri=" + logoutUri +
                                                                 "&state=" + returnUri;
        } else {
            session.invalidate();
            return "redirect:" + returnUri;
        }
    }

    @GetMapping(path = {"/kakao/logout"})
    public String logoutKakao(@RequestParam(name = "state", defaultValue = "home") String state, HttpSession session) {
        session.invalidate();
        return "redirect:" + state;
    }

    @GetMapping(path = {"/sign-in"})
    public String signIn(Model model, HttpSession session,
                         @RequestParam(name = "returnUri", defaultValue = "/home") String returnUri) {
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        model.addAttribute("naverApiKey", naverApi.getNaverApiKey());
        model.addAttribute("naverRedirectUri", naverApi.getNaverRedirectUri());
        model.addAttribute("state", "STATE_STRING");

        returnUri = URLEncoder.encode(returnUri, StandardCharsets.UTF_8);
        model.addAttribute("returnUri", returnUri);
        session.setAttribute("returnUri", returnUri);
        return "account/sign-in";
    }

//    @GetMapping(path = {"/naver/api"})
//    public String naverSignIn(HttpSession session, RedirectAttributes redirectAttributes,
//                              @RequestParam String code,
//                              @RequestParam String state) {
//        String accessToken = naverApi.getAccessToken(code);
//        Map<String, Object> userInfo = naverApi.getUserInfo(accessToken);
//
//        String userId = (String) userInfo.get("userId");
//        String nickname = (String) userInfo.get("nickname");
//        String socialMethod = "naver";
//
//        try {
//            List<UserDto> users = accountService.addAccount(userId, nickname, socialMethod);
//
//            System.out.println("user : " + users.get(0));
//
//            session.setAttribute("accessToken", accessToken);
//            session.setAttribute("user", users.get(0));
//        } catch (Exception e) {
//            boolean isSuccess = false;
//            redirectAttributes.addFlashAttribute("isSuccess", isSuccess);
//            return "redirect:/account/sign-up";
//        }
//
//
//        return "redirect:" + state;
//    }

    @PostMapping(path = {"/block-user"})
    @ResponseBody
    public String blockUser(@RequestParam(name = "boardId") int boardId,
                            HttpSession session) {
        String result = "";
        try {
            List<BoardDto> boards = boardService.findBoardWithBoardId(boardId);
            String blockUser = boards.get(0).getUserId();
            UserDto user = (UserDto) session.getAttribute("user");
            accountService.addBlockUser(blockUser, user.getUserId());
            result = "success";
        } catch (Exception e) {
            result = "fail";
        }

        return result;
    }

    @PostMapping(path = {"/unblock-user"})
    @ResponseBody
    public String unblockUser(@RequestParam(name = "blockUserId") String blockUserId,
                              @RequestParam(name = "userId") String userId) {

        accountService.deleteBlockUser(blockUserId, userId);

        return "success";
    }

    @PostMapping(path = {"/report-content"})
    @ResponseBody
    public String reportContent(@RequestParam(name = "reportCategory") String reportCategory,
                                @RequestParam(name = "contentId") int contentId,
                                HttpSession session) {
        String result = "";
        try {
            UserDto user = (UserDto) session.getAttribute("user");

            if (reportCategory.equals("board")) {
                List<BoardDto> boards = boardService.findBoardWithBoardId(contentId);
                if (boards.get(0).getUserId().equals(user.getUserId())) {
                    return "same";
                }
            }

            if (reportCategory.equals("comment")) {
                CommentDto comment = boardService.findCommentWithCommentId(contentId);
                if (comment.getUserId().equals(user.getUserId())) {
                    return "same";
                }
            }

            accountService.addReport(reportCategory, contentId, user.getUserId());

            result = "success";
        } catch (Exception e) {
            result = "fail";
        }

        return result;
    }

    @PostMapping(path = {"/delete-user"})
    @ResponseBody
    public String deleteUser(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Date expiresAt = user.getExpiresAt();
        Date now = new Date();

        if (now.after(expiresAt)) {
            return "reLogin";
        }

        String accessToken = user.getAccessToken();
        String decryptAccessToken = "";
        try {
            decryptAccessToken = aes.decrypt(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        if (user.getSocialMethod().equals("kakao")) {
            boolean kakaoUnlinked = kakaoApi.unlinkUser(decryptAccessToken);
            if (!kakaoUnlinked) {
                return "fail";
            }
        }

        if (user.getSocialMethod().equals("naver")) {
            boolean naverUnlinked = naverApi.unlinkNaverUser(decryptAccessToken);
            if (!naverUnlinked) {
                return "fail";
            }
        }

        accountService.deleteUser(user.getUserId());
        session.invalidate();

        return "success";
    }

}
