
package com.nasdaq.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@WebAppConfiguration

public class CorsTest
{
  

  
  @Autowired
  private ConfigurableApplicationContext configurableApplicationContext;
   
  @Autowired
  private WebApplicationContext webApplicationContext;
 
  
 @Test 
 public void testCorsOk() throws Exception
 {

   TestPropertyValues testValues = TestPropertyValues.empty();
   testValues = testValues.and("cors.allowed.origins=https://somehost");
   testValues.applyTo(configurableApplicationContext);

   LeiCorsConfiguration leiCorsConfiguration=new LeiCorsConfiguration(configurableApplicationContext.getEnvironment());

   MockMvc mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
       .addFilters(leiCorsConfiguration.corsFilter())
       .apply(SecurityMockMvcConfigurers.springSecurity())
       .build();
   
    mockMvc.perform(get("/api/v1/joke")
       .header("Origin", "https://somehost")
       .header("Access-Control-Request-Method", "GET"))
       .andExpect(status().isOk())
       .andExpect(header().string("Access-Control-Allow-Origin", "https://somehost"));
 }
  
 
 @Test 
 public void testCorsNok() throws Exception
 {

   TestPropertyValues testValues = TestPropertyValues.empty();
   testValues = testValues.and("cors.allowed.origins=https://somehost");
   testValues.applyTo(configurableApplicationContext);
   LeiCorsConfiguration leiCorsConfiguration=new LeiCorsConfiguration(configurableApplicationContext.getEnvironment());


   MockMvc mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
       .addFilters(leiCorsConfiguration.corsFilter())
       .apply(SecurityMockMvcConfigurers.springSecurity())
       .build();
   
    mockMvc.perform(get("/api/v1/joke")
       .header("Origin", "https://otherhost")
       .header("Access-Control-Request-Method", "GET"))
       .andExpect(status().isForbidden())
       .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
 }

}