package com.sullung.springsecurityoauth2jwt.dto;

import java.util.Map;

public class NaverResponseDto implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverResponseDto(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getProfileUrl() {
        return attributes.get("profile_image").toString();
    }
}
