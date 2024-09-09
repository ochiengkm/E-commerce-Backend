package com.microservices.order_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact myContact = new Contact();
        myContact.setName("ochiengkm");
        myContact.setEmail("mosesa.ochieng.com");

        Info information = new Info()
                .title("E-commerce Backend")
                .version("1.1")
                .description("This API exposes endpoints for Order Service")
                .contact(myContact);

        return new OpenAPI().info(information);
    }
}
