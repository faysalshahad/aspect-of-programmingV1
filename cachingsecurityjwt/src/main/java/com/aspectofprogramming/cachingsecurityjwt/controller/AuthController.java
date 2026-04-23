package com.aspectofprogramming.cachingsecurityjwt.controller;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.repository.UserEntityRepository;
import com.aspectofprogramming.cachingsecurityjwt.service.UserService;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.aspectofprogramming.cachingsecurityjwt.security.JWTUtil;
import com.aspectofprogramming.cachingsecurityjwt.service.UserEntityService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

//    @Autowired
//    private UserEntityService userEntityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/reg")
    public UserEntity registerCont(@RequestBody UserEntity userEntity){
//        UserEntity userEntity = new UserEntity();
//
//        userEntity.setUsername(username1);
//        userEntity.setPassword(pasword1);
//        userEntity.setRole(role1);
//
//        userEntityRepository.save(userEntity);



        return userService.createUserEntity(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole());

        //return "User named " + username1 + " has been registered.";

    }


    @PostMapping("/log")
    public String loginCont(@RequestBody UserEntity userEntity){

        return userService.authenticateUser(userEntity);
//
//        if(!passwordEncoder.matches(userEntity.getPassword(), existingUser.getPassword())){
//            throw new RuntimeException("Invalid Password or Credentials");
//        }
//        return jwtUtil.generateAccessToken(existingUser.getUsername());
   }

}
