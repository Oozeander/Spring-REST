package com.example.springweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private CorsConfigProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // register following cors mapping to all routes
                .allowedOrigins(corsProperties.allowedOrigins())
                .allowedMethods(corsProperties.allowedMethods().split(", "))
                .maxAge(corsProperties.maxAge())
                .allowedHeaders(corsProperties.allowedHeaders())
                .exposedHeaders(corsProperties.exposedHeaders());
    }
}
