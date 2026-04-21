package com.aspectofprogramming.cachingsecurityjwt.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aspectofprogramming.cachingsecurityjwt.security.JWTFilter;
import com.aspectofprogramming.cachingsecurityjwt.service.UserEntityService;
import com.google.common.hash.Hashing;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private JWTFilter jwtFilter;

    @Value("${app.pepper.secret}")
    private String pepperSecretKey;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(){
            
            @Override
            public String encode(CharSequence rawPassword){
                // This creates a 256-bit (32 byte) hash that fits perfectly in BCrypt
    String hashedInput = Hashing.sha256()
                                .hashString(rawPassword + pepperSecretKey, StandardCharsets.UTF_8)
                                .toString();
                return super.encode(hashedInput);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword){
                    String hashedInput = Hashing.sha256()
                                .hashString(rawPassword + pepperSecretKey, StandardCharsets.UTF_8)
                                .toString();
                return super.matches(hashedInput, encodedPassword);
            }
        };
    }

}
