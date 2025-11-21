package com.nasdaq;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.datafaker.Faker;

@Configuration
@EnableAutoConfiguration
public class Security {
  

  
  
  @Bean
  @Order(1)
  public SecurityFilterChain testUrl(HttpSecurity http)  throws Exception
  {
    return http
        .securityMatchers(matchers -> matchers
            .requestMatchers("/api/v1/joke"))
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .csrf(CsrfConfigurer::disable)
        .build();
  }  
    
}

