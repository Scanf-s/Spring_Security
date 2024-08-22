package com.sullung.securitypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/loginProc", "/register", "/registerProc").permitAll() // 상단부터 필터를 거친다.
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated() // 위에 명시된 경로 이외에는 로그인 된 사용자만 접근 가능하다
        );

        http.formLogin((form) -> form
                .loginPage("/login") // Login page uri
                .loginProcessingUrl("/loginProc") // Login process uri
                .permitAll() // Any user can access to login page
        );

        http.sessionManagement((session) -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
        );

        http.sessionManagement((session) -> session
                .sessionFixation().changeSessionId());

        http.logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
