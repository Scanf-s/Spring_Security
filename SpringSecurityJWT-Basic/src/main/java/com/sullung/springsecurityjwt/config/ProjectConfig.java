package com.sullung.springsecurityjwt.config;

import com.sullung.springsecurityjwt.jwt.JWTFilter;
import com.sullung.springsecurityjwt.jwt.JWTUtil;
import com.sullung.springsecurityjwt.jwt.LoginFilter;
import com.sullung.springsecurityjwt.jwt.CustomLogoutFilter;
import com.sullung.springsecurityjwt.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@ComponentScan
@EnableWebSecurity
public class ProjectConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Autowired
    public ProjectConfig (
            AuthenticationConfiguration authenticationConfiguration,
            JWTUtil jwtUtil,
            RefreshRepository refreshRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshRepository = refreshRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                })));

        httpSecurity // disable csrf (only in dev)
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity // disable form login
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity // disable http basic auth
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/register", "/refresh").permitAll() // In case of "/", "/login" and "/register" all users can access
                        .requestMatchers("/admin").hasRole("ADMIN") // In case of "/admin" only ROLE_ADMIN can access
                        .anyRequest().authenticated()
                );

        httpSecurity
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .addFilterAt(
                        new LoginFilter(
                                authenticationManager(authenticationConfiguration),
                                jwtUtil,
                                refreshRepository
                        ),
                        UsernamePasswordAuthenticationFilter.class
                );

        httpSecurity
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use STATELESS. Because we don't need sessions in jwt auth
                );

        httpSecurity
                .addFilterBefore(new CustomLogoutFilter(refreshRepository, jwtUtil),
                        LogoutFilter.class
                );

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // For password encryption
        return new BCryptPasswordEncoder();
    }

}
