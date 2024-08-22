package com.sullung.springsecurityjwt.model;

import jakarta.persistence.*;

@Entity
@lombok.Data
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
}
