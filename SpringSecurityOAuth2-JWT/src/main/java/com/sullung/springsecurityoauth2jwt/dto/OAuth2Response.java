package com.sullung.springsecurityoauth2jwt.dto;

public interface OAuth2Response {

    // Provider (naver, google, kakao, ...)
    String getProvider();

    // Provider ID
    String getProviderId();

    // NickName
    String getNickName();

    // Profile Image
    String getProfileImage();

    // Email
    String getEmail();
}
