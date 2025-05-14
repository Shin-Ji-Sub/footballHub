package com.mezzala.oauth2;

import com.mezzala.dto.UserDto;
import com.mezzala.mapper.AccountMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Setter(onMethod_ = {@Autowired})
    private AccountMapper accountMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =
                new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao or naver
        String userNameAttributeName = userRequest
                                            .getClientRegistration()
                                            .getProviderDetails()
                                            .getUserInfoEndpoint()
                                            .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Naver의 경우 response 내부에 사용자 정보가 있음
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            // response에서 꺼낸 값을 다시 flat map으로 변환해서 사용
            attributes = new HashMap<>(response);
            userNameAttributeName = "id";
        }

        // 사용자 고유 ID (네이버는 id, 카카오는 그대로 id)
        String oauthId = String.valueOf(attributes.get("id"));
        List<UserDto> users = accountMapper.selectUser(oauthId);

        if (users.isEmpty() || users == null) {
            String userId = (String) attributes.get("id");
            String nickname = (String) attributes.get("name");
            String socialMethod = "naver".equals(registrationId) ? "naver" : "kakao";
            accountMapper.insertAccount(userId, nickname, socialMethod);
            users = accountMapper.selectUser(userId);
        }

        UserDto user = users.get(0);

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getSession();
        session.setAttribute("user", user);
//        System.out.println("최종 attributes = " + attributes);
//        System.out.println("userNameAttributeName = " + userNameAttributeName);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );
    }

}
