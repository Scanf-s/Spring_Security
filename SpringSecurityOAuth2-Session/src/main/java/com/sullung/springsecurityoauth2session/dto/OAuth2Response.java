package com.sullung.springsecurityoauth2session.dto;

public interface OAuth2Response {

    //Provider (naver, google, kakao, ... )
    String getProvider();

    //providerId that provider issued
    String getProviderId();

    // Email
    String getEmail();

    // Nickname
    String getNickname();

    // Profile Image
    String getProfileImage();
}
