package com.sullung.springsecurityoauth2jwt.model;

import jakarta.persistence.*;

@Entity
@lombok.Data
@Table(name="oauth2_users_jwt")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userIdentifier;
    private String nickname;
    private String email;
    private String profileImage;
    private String role;
}
