package com.sullung.springsecurityoauth2jwt.repository;

import com.sullung.springsecurityoauth2jwt.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

}
