package com.sullung.springsecurityoauth2jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonalController {

    @GetMapping("/")
    public ResponseEntity<String> publicGet() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Public API - GET");
    }

    @GetMapping("/my")
    public ResponseEntity<String> myData() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("My data (private) - GET");
    }
}
