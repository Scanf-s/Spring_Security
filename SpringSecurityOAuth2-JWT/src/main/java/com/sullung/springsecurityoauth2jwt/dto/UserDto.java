package com.sullung.springsecurityoauth2jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String role;
    private String username;
    private String nickname;
    private String email;
    private String profileImage;

}
