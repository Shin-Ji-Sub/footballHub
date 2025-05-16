package com.mezzala.oauth2;

import com.mezzala.common.Aes;
import com.mezzala.common.Util;
import com.mezzala.dto.UserDto;
import com.mezzala.mapper.AccountMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final Aes aes;

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

        } else if ("kakao".equals(registrationId)) {

//            String kakaoId = String.valueOf(attributes.get("id"));

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            Map<String, Object> newAttributes = new HashMap<>();
            newAttributes.put("id", attributes.get("id")); // 최상단 id
            newAttributes.put("nickname", profile.get("nickname")); // profile 안의 nickname

            attributes = newAttributes;

//            attributes = new HashMap<>();
//            attributes.put("id", kakaoId);
//            attributes.put("nickname", profile.get("nickname"));

            userNameAttributeName = "id";

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        }

        // 사용자 고유 ID (네이버는 id, 카카오는 그대로 id)
        String oauthId = String.valueOf(attributes.get("id"));
        List<UserDto> users = accountMapper.selectUser(oauthId);

        // 조회 안될 경우(신규회원)
        if (users.isEmpty() || users == null) {
            String userId = String.valueOf(attributes.get("id"));
            String nickname = registrationId.equals("naver")
                                ? String.valueOf(attributes.get("name"))
                                : String.valueOf(attributes.get("nickname"));
            String socialMethod = registrationId;
            String accessToken = userRequest.getAccessToken().getTokenValue();
            String encryptAccessToken = "";
            try {
                encryptAccessToken = aes.encrypt(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Instant instantExpiresAt = userRequest.getAccessToken().getExpiresAt();
            LocalDateTime expiresAt = LocalDateTime.ofInstant(instantExpiresAt, ZoneId.systemDefault());

            accountMapper.insertAccount(userId, nickname, socialMethod, encryptAccessToken, expiresAt);
            users = accountMapper.selectUser(userId);
        } else {
            // 조회 될 경우(accessToken, expiresAt Update)
            String userId = users.get(0).getUserId();
            String accessToken = userRequest.getAccessToken().getTokenValue();
            String encryptAccessToken = "";
            try {
                encryptAccessToken = aes.encrypt(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Instant instantExpiresAt = userRequest.getAccessToken().getExpiresAt();
            LocalDateTime expiresAt = LocalDateTime.ofInstant(instantExpiresAt, ZoneId.systemDefault());
            accountMapper.updateUserToken(userId, encryptAccessToken, expiresAt);
        }

        UserDto user = users.get(0);

        // 사용자 권한을 DB에서 가져와 Security에 반영
        String roleName = user.getUserRole().getRoleName();
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(roleName));

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getSession();
        session.setAttribute("user", user);
        System.out.println("[USER] : " + user);
//        System.out.println("최종 attributes = " + attributes);
//        System.out.println("userNameAttributeName = " + userNameAttributeName);
        return new DefaultOAuth2User(
                authorities,
                attributes,
                userNameAttributeName
        );
    }

}
