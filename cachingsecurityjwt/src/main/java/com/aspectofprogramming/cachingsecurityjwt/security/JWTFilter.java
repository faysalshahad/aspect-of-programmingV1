package com.aspectofprogramming.cachingsecurityjwt.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    private JWTUtil jwtUtil;
    

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
               String header = request.getHeader("Authorization");

               if(header != null && header.startsWith("Bearer ")){
                String token = header.substring(7);

                String username = jwtUtil.getUsernameFromToken(token);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserEntity user = userEntityRepository.findByUsername(username).orElse(null);
                 if (user !=null && jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                }
               }
               filterChain.doFilter(request, response);
       
    }

    

}
