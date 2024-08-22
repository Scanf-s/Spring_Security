package com.sullung.springsecurityoauth2jwt.dto;

import java.util.Map;
import java.util.Optional;

public class KakaoResponseDto implements OAuth2Response {
    private final Map<String, Object> attributes;

    public KakaoResponseDto(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return Optional.ofNullable(attributes.get("id"))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    public String getNickName() {
        return Optional.ofNullable(attributes.get("kakao_account"))
                .map(account -> (Map<String, Object>) account)
                .map(account -> (Map<String, Object>) account.get("profile"))
                .map(profile -> (String) profile.get("nickname"))
                .orElse(null);
    }

    @Override
    public String getProfileImage() {
        return Optional.ofNullable(attributes.get("kakao_account"))
                .map(account -> (Map<String, Object>) account)
                .map(account -> (Map<String, Object>) account.get("profile"))
                .map(profile -> (String) profile.get("profile_image_url"))
                .orElse(null);
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attributes.get("kakao_account"))
                .map(account -> (Map<String, Object>) account)
                .map(account -> (String) account.get("email"))
                .orElse(null);
    }
}