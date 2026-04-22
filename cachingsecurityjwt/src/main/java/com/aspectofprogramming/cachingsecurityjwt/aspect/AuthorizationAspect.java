package com.aspectofprogramming.cachingsecurityjwt.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    @Before("@annotation(CheckRole)")
    public void checkRole(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new RuntimeException("Access Denied");
        }
    }

}
