package com.aspectofprogramming.cachingsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspectofprogramming.cachingsecurityjwt.security.JWTUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestParam String username){
        return jwtUtil.generateAccessToken(username);
    }

}
