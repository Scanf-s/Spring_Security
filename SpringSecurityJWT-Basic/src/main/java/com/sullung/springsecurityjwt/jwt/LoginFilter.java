package com.sullung.springsecurityjwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sullung.springsecurityjwt.dto.CustomUserDetails;
import com.sullung.springsecurityjwt.model.RefreshEntity;
import com.sullung.springsecurityjwt.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    private final Long accessTokenExpireTime = 60*60*10L;
    private final Long refreshTokenExpireTime = 60*60*24*7L;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        /**
//         * Verify username and password from request (form-data request)
//         */
//
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        System.out.println("usenrame: " + username);
//        System.out.println("password: " + password);
//
//        // In SpringSecurity, username and password have to be stored in token for verification
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//        return authenticationManager.authenticate(authToken);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 데이터 파싱
            Map<String, String> loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);

            String username = loginData.get("username").toString();
            String password = loginData.get("password").toString();

            System.out.println("username: " + username);
            System.out.println("password: " + password);

            // 사용자 이름과 비밀번호를 토큰에 저장
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        /**
         * If user verification succeeded in authenticationManager, SpringSecurity will call createToken method in JwtTokenProvider
         */
        String username = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Generate access, refresh token
        String access = jwtUtil.generateToken("access", username, role, accessTokenExpireTime);
        String refresh = jwtUtil.generateToken("refresh", username, role, refreshTokenExpireTime);

        // save refresh token in database
        jwtUtil.addRefreshEntity(username, refresh, refreshTokenExpireTime);

        // Response access(in header), refresh(with httponly cookie) token
        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(jwtUtil.generateCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws AuthenticationException {
        /**
         * If user verification failed in authenticationManager, SpringSecurity will call this method
         */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
