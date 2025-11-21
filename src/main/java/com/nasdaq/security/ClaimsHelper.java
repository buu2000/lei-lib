package com.nasdaq.security;

import com.nasdaq.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ClaimsHelper {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String GATEWAY_KEY = "gateway";
    private static final String EXTERNAL_GATEWAY_VALUE = "external";
    private static final String INTERNAL_GATEWAY_VALUE = "internal";
    
    public static final String DOCUMENT_REGISTRY_ID_KEY = "document_registry_id";

    private final TokenProvider tokenProvider;

    public ClaimsHelper(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String getSubject(String token)
    {
      return tokenProvider.getSubject(token.replace(BEARER_PREFIX, ""));
    }
    
    public Map<String, Object> getClaims(String token){
        var claims = tokenProvider.getClaims(token.replace(BEARER_PREFIX, ""));
        if (claims == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            
        return claims;
    }

    public boolean isSystemUser(Claims claims){
        return claims.getSubject().equals(TokenProvider.SYSTEM_USER_KEY);
    }

    public boolean isExternalUser(Claims claims){
        var gatewayType = claims.get(GATEWAY_KEY, String.class);
        return gatewayType != null && gatewayType.equals(EXTERNAL_GATEWAY_VALUE);
    }

    public boolean isInternalUser(Claims claims){
        var gatewayType = claims.get(GATEWAY_KEY, String.class);
        return gatewayType != null && gatewayType.equals(INTERNAL_GATEWAY_VALUE);
    }

    public Long getExternalUserId(Claims claims){
        if (!isExternalUser(claims))
            return null;
        
        var subject = claims.getSubject();
        return subject != null ? Long.valueOf(subject) : null;
    }

    public Long getInternalUserId(Claims claims){
        if (isExternalUser(claims))
            return null;
        
        var subject = claims.getSubject();
        return subject != null ? Long.valueOf(subject) : null;
    }

    public Long getUserPersonId(Claims claims){
        return claims.get(TokenProvider.USER_PERSON_ID_KEY, Long.class);
    }
    
    public Long getRepresentedPersonId(Claims claims){
        return claims.get(TokenProvider.REPRESENTED_PERSON_ID_KEY, Long.class);
    }
    
    public boolean containsGivenRights(List<String> rightNames, Claims claims) {
        List<String> rights = List.of(claims.get(TokenProvider.RIGHTS_KEY, String.class).split(","));
        return rights.stream().anyMatch(rightNames::contains);
    }
}
