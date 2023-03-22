package com.example.springweb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring Web App")
                        .description("Optional")
                        .version("1.0")
                        .contact(new Contact().name("Billel KETROUCI").email("billel.ketrouci@gmail.com")))
                .addServersItem(new Server().url("http://localhost:8080/v1")
                        .description("Optional"));
    }
}
