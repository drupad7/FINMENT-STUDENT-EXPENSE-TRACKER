package com.example.expensetracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()  // ✅ Allow API access
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login.disable())  // disable Spring's default login page
                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }
}
