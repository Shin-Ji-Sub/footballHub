package com.mezzala.oauth2;

public interface OAuth2Response {

    // 제공자 (naver, google, ...)
    String getProvider();

    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    // 사용자가 설정한 이름
    String getName();

}
