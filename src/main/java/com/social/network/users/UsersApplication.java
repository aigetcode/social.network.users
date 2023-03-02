package com.social.network.users;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "User Service",
                version = "0.0.2",
                description = "This is project for users",
                contact = @Contact(
                        name = "Alexey Ivanitskiy",
                        email = "to.aleksey.developer@gmail.com"
                )
        )
)
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

}
