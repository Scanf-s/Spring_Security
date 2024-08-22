package com.sullung.springsecurityoauth2sessionclientregistration.dto;

import java.util.Map;

public class GoogleResponseDto implements OAuth2Response{

    private final Map<String, Object> attributes;

    public GoogleResponseDto (Map<String, Object> attributes) {
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
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("given_name").toString();
    }

    @Override
    public String getProfileImage() {
        return attributes.get("picture").toString();
    }
}
