package com.mezzala.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//    @Autowired
//    private HttpSession session;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        String returnUri = (String) session.getAttribute("returnUri");
        if (returnUri == null) {
            returnUri = "/";
        } else {
            session.removeAttribute("returnUri"); // 한 번 쓰고 제거
            returnUri = URLDecoder.decode(returnUri, StandardCharsets.UTF_8);
        }
        response.sendRedirect(returnUri);
    }

}
