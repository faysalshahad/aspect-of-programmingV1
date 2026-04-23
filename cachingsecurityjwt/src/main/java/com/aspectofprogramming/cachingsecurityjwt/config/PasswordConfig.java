package com.aspectofprogramming.cachingsecurityjwt.config;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class PasswordConfig {

    @Value("${app.pepper.secret}")
    private String pepperSecretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                String peppered = Hashing.sha256()
                        .hashString((rawPassword + pepperSecretKey), StandardCharsets.UTF_8)
                        .toString();
                return delegate.encode(peppered);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String peppered = Hashing.sha256()
                        .hashString((rawPassword + pepperSecretKey), StandardCharsets.UTF_8)
                        .toString();
                return delegate.matches(peppered, encodedPassword);
            }
        };
    }
}