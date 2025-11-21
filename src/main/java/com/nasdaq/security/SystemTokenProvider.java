package com.nasdaq.security;

import java.time.temporal.ChronoField;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
// https://medium.com/@SubbuDaduluri/spring-boot-microservice-that-utilizes-oauth2-client-credentials-grant-for-making-external-api-50d1928b93f9
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SystemTokenProvider
{

  private static OAuth2AccessToken oAuth2AccessToken = null;
  private long tokenExpires = 0L;

  private final OAuth2AuthorizedClientManager authorizedClientManager;
  
  public String getTokenValue()
  {

    OAuth2AccessToken token=getToken();
    if(token!=null)
      return token.getTokenValue();
    return null;
  }

  public OAuth2AccessToken getToken()
  {
    if ((oAuth2AccessToken != null) && (tokenExpires < System.currentTimeMillis()))
      return oAuth2AccessToken;
    OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("lei").principal("clientCredentials").build();
    OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(request);
    if (authorizedClient == null || authorizedClient.getAccessToken() == null)
      throw new RuntimeException("Failed to obtain OAuth2 token");
    oAuth2AccessToken = authorizedClient.getAccessToken();
    long issuedAtSec = oAuth2AccessToken.getIssuedAt().getLong(ChronoField.INSTANT_SECONDS);
    long expiredAtSec = oAuth2AccessToken.getExpiresAt().getLong(ChronoField.INSTANT_SECONDS);
    // Subtract 15 seconds for buffer
    tokenExpires = System.currentTimeMillis() + (expiredAtSec - issuedAtSec) - 15;
    return oAuth2AccessToken;
  }

}