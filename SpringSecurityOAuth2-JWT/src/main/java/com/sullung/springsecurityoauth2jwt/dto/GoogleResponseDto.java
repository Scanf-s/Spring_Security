package com.sullung.springsecurityoauth2jwt.dto;

import java.util.Map;

public class GoogleResponseDto implements OAuth2Response {

    private final Map<String, Object> attributes;

    public GoogleResponseDto(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getName() {
        return "Google Nickname";
    }

    @Override
    public String getProfileUrl() {
        return attributes.get("picture").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
