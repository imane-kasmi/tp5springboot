package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour simplifier les tests (à éviter en production)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/h2-console/**").permitAll() // Permettre l'accès à /login, /register et à /h2-console
                        .requestMatchers("/admin").hasRole("ADMIN") // Accès à /admin réservé aux utilisateurs avec le rôle ADMIN
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .formLogin(form -> form
                        .loginPage("/login") // Spécifie la page de login
                        .defaultSuccessUrl("/", true) // Page d'accueil après connexion réussie
                        .permitAll() // Permet l'accès à la page de login pour tous
                )
                .logout(logout -> logout
                        .permitAll() // Permet à tout le monde de se déconnecter
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
