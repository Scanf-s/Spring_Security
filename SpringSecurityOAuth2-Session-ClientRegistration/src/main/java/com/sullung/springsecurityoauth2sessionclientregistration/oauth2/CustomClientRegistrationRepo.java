package com.sullung.springsecurityoauth2sessionclientregistration.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class CustomClientRegistrationRepo {
    /**
     * 소셜 클라이언트 객체를 인메모리로 관리해도
     * google, kakao, naver, github, .. 최대 10개정도밖에 되지 않기 때문에
     * 메모리에서 관리해도 상관 없다.
     */

    private final SocialRegistration socialRegistration;

    @Autowired
    public CustomClientRegistrationRepo (SocialRegistration socialRegistration) {
        this.socialRegistration = socialRegistration;
    }

    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                socialRegistration.naverClientRegistration(),
                socialRegistration.googleClientRegistration()
        );
    }
}
