package com.nasdaq.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  final String securitySchemeName = "bearerAuth";
  
  @Value("${spring.application.name}")
  private String appName;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

        .info(new Info()
            .title("LEI "+appName+"  Service API")
            .version("1.0")
            .description("LEI "+appName+"  Service API documentation")
            .contact(new Contact()
                .name("Development Team")
                .email("info@sets.lv")));
  }
}
