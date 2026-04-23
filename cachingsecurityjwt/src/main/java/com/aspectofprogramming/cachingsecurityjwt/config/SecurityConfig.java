package com.aspectofprogramming.cachingsecurityjwt.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aspectofprogramming.cachingsecurityjwt.security.JWTFilter;
import com.aspectofprogramming.cachingsecurityjwt.service.UserEntityService;
import com.google.common.hash.Hashing;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    @Autowired
//    private UserEntityService userEntityService;

    @Autowired
    private JWTFilter jwtFilter;

//    @Value("${app.pepper.secret}")
//    private String pepperSecretKey;
//
//    // @Bean
//    public class PepperedCustomPasswordEncoder implements PasswordEncoder {
//        private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();
//        // private final String pepperString = pepperSecretKey;
//
//        @Override
//        public String encode(CharSequence rawPassword){
//            // Apply Pepper + SHA-256 logic
//            String pepperedMixString = Hashing.sha256()
//            .hashString((rawPassword + pepperSecretKey), StandardCharsets.UTF_8)
//            .toString();
//
//            // Use the delegate's final method
//            return delegate.encode(pepperedMixString);
//        }
//
//        @Override
//        public boolean matches(CharSequence rawPassword, String encodedPassword){
//            String pepperedMixString = Hashing.sha256()
//            .hashString((rawPassword + pepperSecretKey), StandardCharsets.UTF_8)
//            .toString();
//
//            // Use the delegate's final method
//            return delegate.matches(pepperedMixString, encodedPassword);
//        }
//
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new PepperedCustomPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
        .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

            return httpSecurity.build();
    }
       

}
