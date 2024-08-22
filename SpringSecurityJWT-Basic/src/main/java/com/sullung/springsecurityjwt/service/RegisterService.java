package com.sullung.springsecurityjwt.service;

import com.sullung.springsecurityjwt.dto.RegisterDTO;
import com.sullung.springsecurityjwt.model.UserEntity;
import com.sullung.springsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegisterService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void register(RegisterDTO registerDTO) { // register
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setRole("ROLE_ADMIN");
            userRepository.save(newUser);
        }

    }

}
