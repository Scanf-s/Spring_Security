package com.sullung.springsecurityoauth2jwt.dto;

@lombok.Data
public class UserDto {

    private String role;
    private String userIdentifier;
    private String nickname;
    private String email;
    private String profileImage;

}
