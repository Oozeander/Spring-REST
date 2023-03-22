package com.example.springweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "web.cors")
public record CorsConfigProperties(
        String allowedOrigins,
        String allowedMethods,
        int maxAge,
        String allowedHeaders,
        String exposedHeaders
) {

    @ConstructorBinding
    public CorsConfigProperties(String allowedOrigins, String allowedMethods, int maxAge, String allowedHeaders, String exposedHeaders) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.maxAge = maxAge;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
    }
}
