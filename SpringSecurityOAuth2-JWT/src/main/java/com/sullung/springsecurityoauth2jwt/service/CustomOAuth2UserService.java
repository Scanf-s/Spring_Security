package com.sullung.springsecurityoauth2jwt.service;

import com.sullung.springsecurityoauth2jwt.dto.*;
import com.sullung.springsecurityoauth2jwt.model.UserEntity;
import com.sullung.springsecurityoauth2jwt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserRepository userRepository;

    private static final String NAVER = "naver";
    private static final String GOOGLE = "google";
    private static final String KAKAO = "kakao";

    public CustomOAuth2UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.debug("oAuth2User: {}", oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        logger.debug("registrationId: {}", registrationId);

        OAuth2Response oAuth2Response = getoAuth2Response(registrationId, oAuth2User);

        String userIdentifier = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity user = userRepository.findByUserIdentifier(userIdentifier);
        UserDto userDto = new UserDto();

        if (user == null) { // If new user
            UserEntity newUser = new UserEntity();
            newUser.setUserIdentifier(userIdentifier);
            newUser.setRole("ROLE_USER");
            newUser.setNickname(oAuth2Response.getNickName());
            newUser.setEmail(oAuth2Response.getEmail());
            newUser.setProfileImage(oAuth2Response.getProfileImage());
            userRepository.save(newUser);

        } else { // If user exists
            user.setNickname(oAuth2Response.getNickName());
            user.setEmail(oAuth2Response.getEmail());
            user.setProfileImage(oAuth2Response.getProfileImage());
            userRepository.save(user);
        }

        setUserDto(userDto, userIdentifier, oAuth2Response);
        return new CustomOAuth2User(userDto);
    }

    private void setUserDto(UserDto userDto, String userIdentifier, OAuth2Response oAuth2Response) {
        userDto.setUserIdentifier(userIdentifier);
        userDto.setRole("ROLE_USER");
        userDto.setNickname(oAuth2Response.getNickName());
        userDto.setEmail(oAuth2Response.getEmail());
        userDto.setProfileImage(oAuth2Response.getProfileImage());
    }

    private OAuth2Response getoAuth2Response(String registrationId, OAuth2User oAuth2User) {
        OAuth2Response oAuth2Response = null;
        if(NAVER.equals(registrationId)) {
            oAuth2Response = new NaverResponseDto(oAuth2User.getAttributes());
        } else if (GOOGLE.equals(registrationId)) {
            oAuth2Response = new GoogleResponseDto(oAuth2User.getAttributes());
        } else if (KAKAO.equals(registrationId)) {
            oAuth2Response = new KakaoResponseDto(oAuth2User.getAttributes());
        }
        else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        return oAuth2Response;
    }
}
