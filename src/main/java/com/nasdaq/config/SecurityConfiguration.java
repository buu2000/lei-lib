package com.nasdaq.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration  {



  @Bean
  @Order(1)
  public SecurityFilterChain managment(HttpSecurity http)  throws Exception
  {
    return http
        .securityMatchers(matchers -> matchers
            .requestMatchers("/management/**"))
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .csrf(CsrfConfigurer::disable)
        .build();
  }  
  
  
  @Order(2)
  public SecurityFilterChain swagger(HttpSecurity http)  throws Exception
  {
    return http
        .securityMatchers(matchers -> matchers
           .requestMatchers("/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/v3/api-docs",
                            "/swagger-resources/**",
                            "/configuration/**",
                            "/webjars/**"))
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .csrf(CsrfConfigurer::disable)
        .build();
  }  
    
  
   
  
}
  