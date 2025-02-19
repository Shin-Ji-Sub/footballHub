package com.mezzala.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Data
@Component
public class NaverApi {
    private static final Logger log = LoggerFactory.getLogger(NaverApi.class);
    @Value("${naver.api_key}")
    private String naverApiKey;

    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

    @Value("${naver.client_secret}")
    private String naverClientSecret;

//    @Value("${kakao.logout_redirect_uri}")
//    private String kakaoLogoutRedirectUri;

    // 인가 코드를 받아서 accessToken을 반환
    public String getAccessToken(String code){
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://nid.naver.com/oauth2.0/token";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //필수 쿼리 파라미터 세팅
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(naverApiKey);
            sb.append("&client_secret=").append(naverClientSecret);
            sb.append("&redirect_uri=").append(naverRedirectUri);
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("[NaverApi.getAccessToken] responseCode = {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }

    // accessToken을 받아서 UserInfo 반환
    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://openapi.naver.com/v1/nid/me";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
            log.info("[NaverApi.getUserInfo] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("response").getAsJsonObject();

            String userId = properties.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("name").getAsString();

            userInfo.put("userId", userId);
            userInfo.put("nickname", nickname);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

//    public HashMap<String, String> kakaoLogout() {
//        HashMap<String, String> kakao = new HashMap<>();
//        kakao.put("clientId", kakaoApiKey);
//        kakao.put("logoutUri", kakaoLogoutRedirectUri);
//        return kakao;

//        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

//        try{
//            URL url = new URL(reqUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//
//            int responseCode = conn.getResponseCode();
//            log.info("[KakaoApi.kakaoLogout] responseCode : {}",  responseCode);
//
//            BufferedReader br;
//            if (responseCode >= 200 && responseCode <= 300) {
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            String line = "";
//            StringBuilder responseSb = new StringBuilder();
//            while((line = br.readLine()) != null){
//                responseSb.append(line);
//            }
//            String result = responseSb.toString();
//            log.info("kakao logout - responseBody = {}", result);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
