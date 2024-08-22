package com.sullung.springsecurityoauth2session.service;

import com.sullung.springsecurityoauth2session.dto.CustomOAuth2User;
import com.sullung.springsecurityoauth2session.dto.GoogleResponseDto;
import com.sullung.springsecurityoauth2session.dto.NaverResponseDto;
import com.sullung.springsecurityoauth2session.dto.OAuth2Response;
import com.sullung.springsecurityoauth2session.model.UserEntity;
import com.sullung.springsecurityoauth2session.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // provider가 naver, google, kakao, ... 중 어떤건지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponseDto(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponseDto(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // providerId, email, nickname, profileImage
        System.out.println(oAuth2Response.getProviderId());
        System.out.println(oAuth2Response.getEmail());
        System.out.println(oAuth2Response.getNickname());
        System.out.println(oAuth2Response.getProfileImage());

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        UserEntity user = userRepository.findByUsername(username);
        String role = null;
        if (user == null) { // If new user
            user = new UserEntity();
            role = "ROLE_USER";

            user.setUsername(oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId());
            user.setNickname(oAuth2Response.getNickname());
            user.setEmail(oAuth2Response.getEmail());
            user.setProfileUrl(oAuth2Response.getProfileImage());
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setLastLoginAt(new Date());
            user.setRole(role);
            userRepository.save(user);
        }
        else { // If existing user
            user.setNickname(oAuth2Response.getNickname());
            user.setEmail(oAuth2Response.getEmail());
            user.setProfileUrl(oAuth2Response.getProfileImage());
            user.setUpdatedAt(new Date());
            user.setLastLoginAt(new Date());
            role = user.getRole();
            userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }

}
