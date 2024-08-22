package com.sullung.securitypractice.model;

import jakarta.persistence.*;

@Entity
@lombok.Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String role;
}
