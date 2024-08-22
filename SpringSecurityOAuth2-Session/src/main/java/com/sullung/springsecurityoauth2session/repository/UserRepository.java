package com.sullung.springsecurityoauth2session.repository;

import com.sullung.springsecurityoauth2session.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
