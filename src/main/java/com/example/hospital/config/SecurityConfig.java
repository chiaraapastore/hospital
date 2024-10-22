package com.example.hospital.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //perchè usare solo questa

//Questa classe configura la sicurezza dell'applicazione
public class SecurityConfig {

    private final KeycloakJwtTokenConverter keycloakJwtTokenConverter;

    public SecurityConfig(KeycloakJwtTokenConverter keycloakJwtTokenConverter) {
        this.keycloakJwtTokenConverter = keycloakJwtTokenConverter;
    }

    // Iniezione della dipendenza JwtAuthenticationFilter
    //Inietta due componenti: un filtro di autenticazione JWT (JwtAuthenticationFilter) e il convertitore di token JWT (KeycloakJwtTokenConverter).

    //Configura la catena di filtri di sicurezza. Disabilita il CSRF (perché si usano i token JWT), definisce regole di accesso per endpoint specifici e aggiunge il filtro di autenticazione JWT prima del filtro di autenticazione di default.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable()) // Disabilito la protezione CSRF poiché usiamo JWT
                .authorizeRequests()
                .requestMatchers("/api/login/**").permitAll() // Accesso aperto a tutti per endpoint pubblici
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/create/users/mongo").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Solo gli utenti con ruolo ADMIN possono accedere agli endpoint admin
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // Gli utenti e admin possono accedere agli endpoint user
                .anyRequest().authenticated(); // Tutte le altre richieste devono essere autenticate
        return http.build();
    }



    //csrf: CSRF (Cross-Site Request Forgery) è un tipo di attacco in cui un utente autenticato in un sito web viene indotto, senza saperlo, a eseguire un'azione non desiderata su un'altra applicazione web, per esempio, cambiando una password o effettuando un trasferimento di denaro. L'attaccante sfrutta l'autenticazione attiva dell'utente per inviare richieste dannose.
}

