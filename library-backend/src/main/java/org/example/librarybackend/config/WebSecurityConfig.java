//package org.example.librarybackend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/books/**").permitAll()  // Allow access to /books endpoint without authentication
//                .anyRequest().authenticated()  // Require authentication for other endpoints
//                .and()
//                .formLogin();  // Enables the default login page
//        return http.build();
//    }
//}