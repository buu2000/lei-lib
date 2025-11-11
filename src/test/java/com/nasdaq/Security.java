package com.nasdaq;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.datafaker.Faker;

@Configuration
public class Security {
  
  
  @Bean
  public SecurityFilterChain configure(HttpSecurity http)  throws Exception
  {
      http
        .authorizeHttpRequests(authManager -> {
          authManager.requestMatchers("/api/v1/joke")
            .permitAll()
            .anyRequest()
            .authenticated();
        });
      return http.build();
  }

    
}

