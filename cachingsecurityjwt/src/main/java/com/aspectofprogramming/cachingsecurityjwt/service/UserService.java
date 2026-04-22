package com.aspectofprogramming.cachingsecurityjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.repository.UserEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    public String getData(){
        return "Protected Data";
    }

    // Transactional Method
    @Transactional
    public UserEntity createUserEntity(String username1, String password1, String role1){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username1);
        userEntity.setPassword(password1);
        userEntity.setRole(role1);

        userEntityRepository.save(userEntity);

        // Simulating failure
        if(username1.equals(password1)){
            System.out.println("Username and Password cannot be same.");
            throw new RuntimeException("Forcing Rollback");
        }
        return userEntity;
    }

    // Cacheable Method
    // This now works with Spring's Cache Manager
    @Cacheable(value = "userList", key = "#username")
    public UserEntity getUserEntity(String username){

        System.out.println("Fetching from database: ");

        return userEntityRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("No User data found"));

    }

    //Cache Update
    @Transactional
    @CachePut(value = "userList", key = "#username")
    public UserEntity updateUserEntity(String username){

        UserEntity userEntity = userEntityRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("No update made because User does not exist."));

        String oldName = userEntity.getUsername();

        if(oldName.equals(username)){
            System.out.println("New Username cannot be same as the old one.");
            throw new RuntimeException("Forcing Rollback. User data won't be updated");
        }


        userEntity.setUsername(username);

        System.out.println("Updated name is : " + username);

        return userEntityRepository.save(userEntity);

    }

    //Cache Remove
    @CacheEvict(value = "userList", key = "#username")
    public void deleteUserEntity(String username){
        userEntityRepository.findByUsername(username)
        .ifPresent(userEntityRepository::delete);
    }



}
