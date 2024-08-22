package com.sullung.springsecurityoauth2session.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@lombok.Data
@Table(name = "oauth2_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    private String username;

    private String nickname;

    private String email;

    @Column(name = "profile_url")
    private String profileUrl;

    private String role;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "last_login_at")
    private Date lastLoginAt;
}
