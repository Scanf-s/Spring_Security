package com.sullung.springsecurityoauth2jwt.dto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDto userDto;

    public CustomOAuth2User(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(userDto::getRole);
        return collection;
    }

    @Override
    public String getName() {
        return userDto.getNickname();
    }

    public String getUsername() {
        return userDto.getUsername();
    }
}
