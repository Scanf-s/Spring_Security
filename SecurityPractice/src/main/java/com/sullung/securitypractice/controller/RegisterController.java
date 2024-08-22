package com.sullung.securitypractice.controller;

import com.sullung.securitypractice.service.RegisterService;
import com.sullung.securitypractice.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/registerProc")
    public String joinProc(UserDTO userDTO) {
        System.out.println(userDTO.getUsername());
        registerService.registerUser(userDTO);
        return "redirect:/login";
    }
}
