package com.aspectofprogramming.cachingsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.service.UserService;

@RestController
@RequestMapping("/went")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public String getAllData(){
        return  userService.getData();
    }

    @PostMapping("/save")
    public UserEntity saveUserEntity(@RequestParam String username2, @RequestParam String password2, @RequestParam String role2 ){
        return userService.createUserEntity(username2, password2, role2);
    }

    @GetMapping("/{username3}")
    public UserEntity getUser(@PathVariable String username3){
        return userService.getUserEntity(username3);

    }

}
