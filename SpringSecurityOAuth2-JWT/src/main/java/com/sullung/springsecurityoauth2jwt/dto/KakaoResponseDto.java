package com.sullung.springsecurityoauth2jwt.dto;

import java.util.Map;
import java.util.Optional;

public class KakaoResponseDto implements OAuth2Response {
    private final Map<String, Object> attributes;
    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccount;

    public KakaoResponseDto(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }

    @Override
    public String getName() {
        return properties != null ? (String) properties.get("nickname") : null;
    }

    @Override
    public String getProfileUrl() {
        return properties != null ? (String) properties.get("profile_image") : null;
    }
}