package com.mezzala.controller;

import com.mezzala.common.KakaoApi;
import com.mezzala.common.NaverApi;
import com.mezzala.dto.UserDto;
import com.mezzala.service.AccountService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = {"account"})
public class AccountController {

    private final KakaoApi kakaoApi;
    private final NaverApi naverApi;

    @Setter(onMethod_ = {@Autowired})
    private AccountService accountService;

    @GetMapping(path = {"/sign-up"})
    public String signUp(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        model.addAttribute("naverApiKey", naverApi.getNaverApiKey());
        model.addAttribute("naverRedirectUri", naverApi.getNaverRedirectUri());
        return "/account/sign-up";
    }

    @RequestMapping(path = {"/kakao/api"})
    public String kakaoSignIn(@RequestParam String code, HttpSession session) {
        String accessToken = kakaoApi.getAccessToken(code);
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String userId = (String) userInfo.get("userId");
        String nickname = (String) userInfo.get("nickname");
        String socialMethod = "kakao";

//        System.out.println("nickname : " + nickname);
//        System.out.println("userId : " + userId);
//        System.out.println("accessToken : " + accessToken);

        List<UserDto> users = accountService.addAccount(userId, nickname, socialMethod);

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("user", users.get(0));

        return "redirect:/";
    }

    @GetMapping(path = {"/log-out"})
    public String logOut(HttpSession session, @RequestParam(name="socialMethod") String socialMethod) {

//        String accessToken = (String) session.getAttribute("accessToken");
        if (socialMethod.equals("kakao")) {
            HashMap<String, String> kakao = kakaoApi.kakaoLogout();
            String clientId = kakao.get("clientId");
            String logoutUri = kakao.get("logoutUri");
            session.invalidate();

            return "redirect:https://kauth.kakao.com/oauth/logout?client_id=" + clientId + "&logout_redirect_uri=" + logoutUri;
        } else {
            session.invalidate();
            return "redirect:/";
        }
    }

    @GetMapping(path = {"/kakao/logout"})
    public String logoutKakao() {
        return "redirect:/";
    }

    @GetMapping(path = {"/sign-in"})
    public String signIn(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        model.addAttribute("naverApiKey", naverApi.getNaverApiKey());
        model.addAttribute("naverRedirectUri", naverApi.getNaverRedirectUri());
        model.addAttribute("state", "STATE_STRING");
        return "/account/sign-in";
    }

    @GetMapping(path = {"/naver/api"})
    public String naverSignIn(HttpSession session, @RequestParam String code) {
        String accessToken = naverApi.getAccessToken(code);
        Map<String, Object> userInfo = naverApi.getUserInfo(accessToken);

        String userId = (String) userInfo.get("userId");
        String nickname = (String) userInfo.get("nickname");
        String socialMethod = "naver";

        System.out.println("nickname : " + nickname);
        System.out.println("userId : " + userId);
        System.out.println("accessToken : " + accessToken);

        List<UserDto> users = accountService.addAccount(userId, nickname, socialMethod);

        System.out.println("user : " + users.get(0));

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("user", users.get(0));

        return "redirect:/";
    }

}
