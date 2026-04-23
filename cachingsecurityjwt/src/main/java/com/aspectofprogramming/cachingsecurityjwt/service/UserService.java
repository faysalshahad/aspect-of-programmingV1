package com.aspectofprogramming.cachingsecurityjwt.service;

import com.aspectofprogramming.cachingsecurityjwt.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.repository.UserEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public String getData(){
        return "Protected Data";
    }

    // Transactional Method
    @Transactional
    public UserEntity createUserEntity(String username1, String password1, String role1){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username1);
        // Encodes using the custom Peppered logic from PasswordConfig
        userEntity.setPassword(passwordEncoder.encode(password1));

        // Standardize Role format
        String formattedRole = role1.toUpperCase();
        if(!formattedRole.startsWith("ROLE_")){
            formattedRole = "ROLE_" + formattedRole;

        }
        userEntity.setRole(formattedRole);

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
//    @Cacheable(value = "userList", key = "#username")
    public String authenticateUser(UserEntity userEntity){

       // System.out.println("Fetching from database: ");

//        UserEntity exisitingUser = userEntityRepository.findByUsername(userEntity.getUsername())
//                        .orElseThrow(()-> new RuntimeException("User not found on that name " + userEntity.getUsername()));

        // Calling the cacheable method instead of the repository directly
        UserEntity existingUser = getUserEntity(userEntity.getUsername());
        if(passwordEncoder.matches(userEntity.getPassword(), existingUser.getPassword())){
            return jwtUtil.generateAccessToken(userEntity.getUsername());
        } else {
            throw new RuntimeException("User name or credential does not match with existing database.");
        }
//        return userEntityRepository.findByUsername(username)
//        .orElseThrow(() -> new RuntimeException("No User data found"));

    }

    // This method will now store the result in "userList" cache
// If the same username is requested again, it returns the cached UserEntity
    @Cacheable(value = "userList", key = "#username")
    public UserEntity getUserEntity(String username) {
        // This log is the proof of caching!
        System.out.println(" [PostgreSQL DB CHECK] Fetching from database for user: " + username);
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No User data found with username: " + username));
    }

    //Cache Update
    @Transactional
    @CachePut(value = "userList", key = "#userEntity.username")
    public UserEntity updateUserEntity(UserEntity userEntity){
        System.out.println("[Postgresql DB UPDATE] Updating user in DB and Cache " + userEntity.getUsername());
        UserEntity existingUser = userEntityRepository.findByUsername(userEntity.getUsername())
        .orElseThrow(() -> new RuntimeException("No update made because User does not exist."));
//
//        String oldPassword = userEntity.getUsername();
//
//        if(oldName.equals(username)){
//            System.out.println("New Username cannot be same as the old one.");
//            throw new RuntimeException("Forcing Rollback. User data won't be updated");
//        }
//
//
//        userEntity.setUsername(username);
//
//        System.out.println("Updated name is : " + username);

        String formattedRole = userEntity.getRole().toUpperCase();
        if(!formattedRole.startsWith("ROLE_")){
            formattedRole = "ROLE_" + formattedRole;

        }

        existingUser.setRole(formattedRole);

        return userEntityRepository.save(existingUser);

    }

    //Cache Remove
    @CacheEvict(value = "userList", key = "#username")
    public String deleteUserEntity(String username){
        System.out.println("[CACHE EVICT] Removing user from cache");
        userEntityRepository.findByUsername(username)
        .ifPresent(userEntityRepository::delete);
        return "User " + username + " has been deleted from the database and cache";
    }



}
