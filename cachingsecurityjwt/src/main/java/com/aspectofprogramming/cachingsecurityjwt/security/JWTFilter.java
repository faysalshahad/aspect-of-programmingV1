package com.aspectofprogramming.cachingsecurityjwt.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aspectofprogramming.cachingsecurityjwt.entity.UserEntity;
import com.aspectofprogramming.cachingsecurityjwt.repository.UserEntityRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

    @Autowired
    private JWTUtil jWTUtil;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
               String header = request.getHeader("Authorization");

               if(header != null && header.startsWith("Bearer ")){
                String token = header.substring(7);

                String username = jWTUtil.getUsernameFromToken(token);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserEntity user = userEntityRepository.findByUsername(username).orElse(null);
                 if (user !=null && jWTUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                }
               }
               filterChain.doFilter(request, response);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }

    

}
