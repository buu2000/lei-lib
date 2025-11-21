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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nasdaq.security.jwt.JwtAuthConverter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import  org.springframework.web.cors.CorsConfiguration;
import org.springframework.core.annotation.Order;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration  {


  private final JwtAuthConverter jwtAuthConverter;
  
  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  private String jwkSetUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.client-issuer-uri}")
  private String clientIssuerUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.client-jwk-set-uri}")
  private String clientjwkSetUri;
  
  

  
  
  @Bean
  @Order(0)
  public SecurityFilterChain leiFilterChain(HttpSecurity http) throws Exception {
    
    return http
        .securityMatchers(matchers -> matchers
            .requestMatchers("/**"))
    .oauth2ResourceServer(oauth2 -> oauth2
            .authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver()))
        .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(CsrfConfigurer::disable)
        .build();
  }

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
    
  
//https://github.com/spring-projects/spring-security/issues/14677#issuecomment-1978288891
//https://stackoverflow.com/questions/65398610/extract-authentication-from-bearer-token-after-issuer-resolver-with-spring-secur
   @Bean
   public JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver() {
       Map<String , String> issuerToJwkSetUri = new TreeMap<String , String>() {{
           put(issuerUri,jwkSetUri);
           put(clientIssuerUri,clientjwkSetUri);
       }};
       log.info("issuerUri="+issuerUri);
       log.info("jwkSetUri="+jwkSetUri);
       log.info("clientIssuerUri="+clientIssuerUri);
       log.info("clientjwkSetUri="+clientjwkSetUri);
       return new JwtIssuerAuthenticationManagerResolver(
               (issuer) -> {
                   NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(issuerToJwkSetUri.get(issuer)).build();
                   jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
                   JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
                   authenticationProvider.setJwtAuthenticationConverter(jwtAuthConverter);
                   return authenticationProvider::authenticate;
               }
       );
   }
  
  
}
  