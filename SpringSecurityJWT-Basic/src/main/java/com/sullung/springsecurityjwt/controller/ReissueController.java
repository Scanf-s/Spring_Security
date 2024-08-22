package com.sullung.springsecurityjwt.controller;

import com.sullung.springsecurityjwt.jwt.JWTUtil;
import com.sullung.springsecurityjwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    private final Long accessTokenExpireTime = 60*60*10L;
    private final Long refreshTokenExpireTime = 60*60*24*7L;

    @Autowired
    public ReissueController(
            JWTUtil jwtUtil,
            RefreshRepository refreshRepository
    ){
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){

        //get refresh token from request
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return new ResponseEntity<>("refresh token not found", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // Check token if it is real token
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("not refresh token", HttpStatus.BAD_REQUEST);
        }

        // Check token if it stored in database
        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {
            return new ResponseEntity<>("not exist refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.generateToken("access", username, role, accessTokenExpireTime);
        String newRefreshToken = jwtUtil.generateToken(
                "refresh", username, role, refreshTokenExpireTime); // Refresh Rotate

        // delete old refresh token
        refreshRepository.deleteByRefresh(refreshToken);
        jwtUtil.addRefreshEntity(username, newRefreshToken, refreshTokenExpireTime);

        // response
        response.setHeader("access", newAccessToken);
        response.addCookie(new Cookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
