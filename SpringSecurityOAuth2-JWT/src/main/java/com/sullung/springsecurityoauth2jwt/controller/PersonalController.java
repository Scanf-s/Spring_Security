package com.sullung.springsecurityoauth2jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonalController {

    @GetMapping
    public ResponseEntity<String> personalGet() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Private API - GET");
    }
}
