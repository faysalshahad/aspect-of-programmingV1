package com.aspectofprogramming.cachingsecurityjwt.service;

import java.util.List;

import com.aspectofprogramming.cachingsecurityjwt.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;

@Component
public class UserEntityService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found on name " + username));
        return new User(
            userEntity.getUsername(),
            userEntity.getPassword(),
            List.of(new SimpleGrantedAuthority(userEntity.getRole()))

        );

        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}
