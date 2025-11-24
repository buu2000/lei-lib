package com.nasdaq.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.web.cors.CorsConfiguration;

import com.nasdaq.security.jwt.JwtAuthConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class HttpSecurityConfig 
{


  
  @Autowired
  private final LeiCorsConfiguration leiCorsConfiguration;
  
  @Autowired
  private final JwtAuthConverter jwtAuthConverter;

  
  @Autowired
  public @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri;

  @Autowired
   public @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri;

  @Autowired
  
  public @Value("${spring.security.oauth2.resourceserver.jwt.client-issuer-uri}") String clientIssuerUri;

  @Autowired
  
  public @Value("${spring.security.oauth2.resourceserver.jwt.client-jwk-set-uri}") String clientjwkSetUri;
 

  // https://github.com/spring-projects/spring-security/issues/14677#issuecomment-1978288891
  // https://stackoverflow.com/questions/65398610/extract-authentication-from-bearer-token-after-issuer-resolver-with-spring-secur
  @Bean
  public JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver()
  {
    Map<String, String> issuerToJwkSetUri = new TreeMap<String, String>()
    {
      {
        put(issuerUri, jwkSetUri);
        put(clientIssuerUri, clientjwkSetUri);
      }
    };
    return new JwtIssuerAuthenticationManagerResolver((issuer) ->
    {
      NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(issuerToJwkSetUri.get(issuer)).build();
      jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
      JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
      authenticationProvider.setJwtAuthenticationConverter(jwtAuthConverter);
      return authenticationProvider::authenticate;
    });
  }
  
  private static final String HTTP_SECURITY_DEFAULT_BEAN_NAME = "org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration.httpSecurity";

  
  @Bean
  public BeanPostProcessor httpSecurityBeanPostProcessor() {
      return new BeanPostProcessor() {

          @SuppressWarnings("unused")
          @Override
          public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
              if (bean instanceof HttpSecurity && HTTP_SECURITY_DEFAULT_BEAN_NAME.equals(beanName)) {
                  HttpSecurity http = (HttpSecurity) bean;
                  try {
                    http.cors(Customizer.withDefaults())
                    .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver()))
                    .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(CsrfConfigurer::disable);

                  } catch (Exception e) {
                      log.error(e.getMessage(),e);
                      throw new RuntimeException(e);
                  }
              }
              return bean;
          }
      };
  }


}
