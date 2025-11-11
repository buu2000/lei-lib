package com.nasdaq;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.datafaker.Faker;

@RestController
public class Controller {
  
  private static final Faker FAKER = new Faker();

  @GetMapping(value = "/api/v1/joke")
  public ResponseEntity<JokeResponse> generate() {
      String joke = "test";
      String remainingLimit = FAKER.number().digit();

      return ResponseEntity.ok()
        .header("X-Rate-Limit-Remaining", remainingLimit)
        .body(new JokeResponse(joke));
  }

  record JokeResponse(String joke) {};

  
}

