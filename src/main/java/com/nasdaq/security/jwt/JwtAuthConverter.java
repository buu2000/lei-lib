package com.nasdaq.security.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    System.out.println("convert JWT "+jwt.toString());
    Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);
    return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(JwtClaimNames.SUB));
  }

  private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
    System.out.println("JWT extractResourceRoles "+jwt.toString());
    Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
    Collection<String> resourceRoles;
    if (resourceAccess == null || (resourceRoles = (Collection<String>) resourceAccess.get("roles")) == null) {
      return Collections.emptySet();
    }
    return resourceRoles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toSet());
  }

}

