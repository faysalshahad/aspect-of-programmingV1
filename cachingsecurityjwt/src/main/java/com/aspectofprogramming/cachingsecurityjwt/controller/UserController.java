package com.aspectofprogramming.cachingsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.service.UserService;

@RestController
@RequestMapping("/see")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public String getAllData(){
        return  userService.getData();
    }

//    @PostMapping("/save")
//    public UserEntity saveUserEntity(@RequestParam String username2, @RequestParam String password2, @RequestParam String role2 ){
//        return userService.createUserEntity(username2, password2, role2);
//    }

//    @GetMapping("/{username3}")
//    public UserEntity getUser(@PathVariable String username3){
//        return userService.getUserEntity(username3);
//
//    }

    // TEST CACHEABLE: Call this twice for the same username.
    // The first time hits DB, the second time is instant.

    @GetMapping("/user/{username}")
    public UserEntity getUser(@PathVariable String username){
        return userService.getUserEntity(username);
    }

    // TEST CACHEPUT: This updates the database AND refreshes the cache.
    @PostMapping("/update")
    public UserEntity update(@RequestBody UserEntity userEntity){
        return userService.updateUserEntity(userEntity);
    }

    @PostMapping("/clear")
    public String clearCache(@RequestBody UserEntity userEntity){
        return userService.deleteUserEntity(userEntity.getUsername());

    }



}
