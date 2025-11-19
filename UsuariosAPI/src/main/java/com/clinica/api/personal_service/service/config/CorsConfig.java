package com.clinica.api.personal_service.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a TODAS las rutas de la API
                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173") // Permite ambas versiones
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos los verbos HTTP
                        .allowedHeaders("*") // Permite todas las cabeceras
                        .allowCredentials(true); // Permite credenciales/cookies si fuera necesario
            }
        };
    }
}