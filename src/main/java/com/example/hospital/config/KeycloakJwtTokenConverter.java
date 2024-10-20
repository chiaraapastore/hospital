package com.example.hospital.config;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.bson.json.StrictJsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.annotation.Collation;
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
@RequiredArgsConstructor
//Converte i token JWT in una collezione di GrantedAuthority, che rappresentano le autorizzazioni dell'utente nel sistema.
public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    //Estrazione dal token
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "role_prefix";
    // Istanziazione diretta del converter
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final TokenConverterPropierties tokenConverterPropierties;

    //Conversione
    //Converte un token JWT in una lista di GrantedAuthority. Estrae i ruoli dai claim resource_access e li converte in oggetti GrantedAuthority.
    @Override
    public Collection<GrantedAuthority> convert(@NotNull Jwt jwt) {
        LinkedList<GrantedAuthority> result = new LinkedList<>();
        try {
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS);
            if (resourceAccess != null) {
                Map<String, Object> clientIdMap = (Map<String, Object>) resourceAccess.get(tokenConverterPropierties.getResourceId());
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
            result.addAll(jwtGrantedAuthoritiesConverter.convert(jwt));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

