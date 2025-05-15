package com.mezzala.config;

import com.mezzala.oauth2.CustomOAuth2UserService;
import com.mezzala.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.net.URLEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/board/*write*", "/board/*modify*", "/board/*delete*",
                                        "/request/*write*", "/request/*modify*", "/request/*delete*",
                                        "/mypage/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/oauth2/save-return-uri").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                            .loginPage("/account/sign-in")
                            .userInfoEndpoint(userInfo -> userInfo
                                    .userService(customOAuth2UserService)
                            )
                            .successHandler(oAuth2LoginSuccessHandler)
                            .failureHandler((request, response, exception) -> {
                                exception.printStackTrace(); // 콘솔 로그
                                response.sendRedirect("/account/sign-in?error=" + URLEncoder.encode(exception.getMessage(), "UTF-8"));
                            })
                )
                .logout((logout) -> logout
                        .logoutUrl("/account/log-out")
                        .invalidateHttpSession(true) // 로그아웃시 로그인 했던 모든 정보를 삭제
                        .deleteCookies("JSESSIONID") // 로그아웃시 JSESSIONID 쿠키 삭제 톰캣이 만든 세션Id 이름 = JSESSIONID
                        .logoutSuccessUrl("/home")  // 로그아웃시 리다이렉트할 URL
                );

        return http.build();
    }
}
