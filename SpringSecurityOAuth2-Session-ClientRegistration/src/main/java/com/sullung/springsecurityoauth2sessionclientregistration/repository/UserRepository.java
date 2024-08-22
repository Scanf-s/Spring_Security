package com.sullung.springsecurityoauth2sessionclientregistration.repository;

import com.sullung.springsecurityoauth2sessionclientregistration.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
