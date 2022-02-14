package com.example.springsecuritysocial.repository;

import com.example.springsecuritysocial.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
