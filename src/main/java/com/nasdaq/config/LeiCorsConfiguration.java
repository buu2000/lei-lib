package com.nasdaq.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import  org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class LeiCorsConfiguration  {
  
  public LeiCorsConfiguration(ConfigurableEnvironment environment)
  {
    this.environment=environment;
  }
  
  public LeiCorsConfiguration()
  {
  }
  
  
  @Autowired
  private ConfigurableEnvironment environment;
  
  @Bean
  public CorsFilter corsFilter() {
    log.info("CORS ORIGINS PATTERNS: "+environment.getProperty("cors.allowed.origins"));
    String[] origins=environment.getProperty("cors.allowed.origins").split(",");
    CorsConfiguration corsConfiguration=new CorsConfiguration();
    // corsConfiguration.setAllowedOrigins(Arrays.asList(origins));
    corsConfiguration.setAllowedOriginPatterns(Arrays.asList(origins));
    corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
    corsConfiguration.setAllowedHeaders(List.of("*"));
    corsConfiguration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
    corsConfiguration.setMaxAge(-1L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(source);
  }


  
}
