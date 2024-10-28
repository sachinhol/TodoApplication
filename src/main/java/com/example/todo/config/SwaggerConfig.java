package com.example.todo.config;/*
 * Author: Sachin
 * Date: 28-Oct-24
 * Swagger Config for customization
 */

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI myCustomSwaggerConfig(){
        return new OpenAPI()
                .info(new Info().title("ToDo MVC APIs")
                        .description("API doc for managing to do task")
                        .version("1.0")
                        .contact(new Contact().name("Sachin")))
                .servers(List.of(new Server().url("http://localhost:8081").description("Local Server")));
    }
}
