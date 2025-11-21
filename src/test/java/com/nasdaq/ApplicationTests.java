package com.nasdaq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@AutoConfigureMockMvc
@SpringBootTest
  class ApplicationTests {
  
  @MockBean
  private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
  
  @MockBean
  private ClientRegistrationRepository clientRegistrationRepository;
  
    @Test
    void contextLoads() {
    }

}

