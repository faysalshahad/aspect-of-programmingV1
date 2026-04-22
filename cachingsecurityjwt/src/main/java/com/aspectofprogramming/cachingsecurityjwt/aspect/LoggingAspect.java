package com.aspectofprogramming.cachingsecurityjwt.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Apply to all service methods
    @Pointcut("execution(* com.aspectofprogramming.cachingsecurityjwt.service*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void before(JoinPoint joinPoint){
        System.out.println("Entering: " + joinPoint.getSignature());
    }

    @AfterReturning(value = "serviceMethods()", returning="result")
    public void after(JoinPoint joinPoint, Object result){
        System.out.println("Completed: " + joinPoint.getSignature());
    }

    @AfterThrowing(value = "serviceMethods()", throwing="ex")
    public void execption(JoinPoint joinPoint, Exception ex){
        System.out.println("Exception in: " + joinPoint.getSignature());
    }

    //Get Logged-in User in AOP
    @Before("serviceMethods()")
    public void logUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            System.out.println("User : " + auth.getName());
        }

    }



}
