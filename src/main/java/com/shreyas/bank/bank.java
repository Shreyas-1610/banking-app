package com.shreyas.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Banking Application",
		description = "Rest APIs for Bank of Shreyas",
		version = "v1.0",
		contact = @Contact(
			name = "Shreyas Kulkarni",
			email = "kshreyask1@gmail.com",
			url = "https://github.com/Shreyas-1610"
		),
		license = @License(
			name = "Shreyas",
			url = "https://github.com/Shreyas-1610"
		)
	),
	externalDocs = @ExternalDocumentation(
		description = "Bank of Shreyas App Documentation",
		url = "https://github.com/Shreyas-1610"
	)
)
public class bank {

	public static void main(String[] args) {
		SpringApplication.run(bank.class, args);
	}

}
