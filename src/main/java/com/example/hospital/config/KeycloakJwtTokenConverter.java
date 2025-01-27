package com.example.hospital.config;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component

public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "role_prefix";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    private final TokenConverterProperties tokenConverterProperties;

    public KeycloakJwtTokenConverter(JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter, TokenConverterProperties tokenConverterProperties) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
        this.tokenConverterProperties = tokenConverterProperties;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        LinkedList<GrantedAuthority> result = new LinkedList<>();

        Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS);
        if (resourceAccess != null) {
            Map<String, Object> clientIdMap = (Map<String, Object>) resourceAccess.get(tokenConverterProperties.getResourceId());
            if (clientIdMap != null) {
                List<String> roles = (List<String>) clientIdMap.get(ROLES);
                if (roles != null) {
                    Collection<GrantedAuthority> resourceRoles = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                            .collect(Collectors.toList());
                    result.addAll(resourceRoles);
                }
            }
        }

        Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS);
        if (realmAccess != null) {
            List<String> realmRoles = (List<String>) realmAccess.get(ROLES);
            if (realmRoles != null) {
                Collection<GrantedAuthority> realmAuthorities = realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                        .collect(Collectors.toList());
                result.addAll(realmAuthorities);
            }
        }

        return result;
    }


}

