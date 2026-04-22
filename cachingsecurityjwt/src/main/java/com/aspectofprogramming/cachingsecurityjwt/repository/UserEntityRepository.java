package com.aspectofprogramming.cachingsecurityjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;

// @Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

}
