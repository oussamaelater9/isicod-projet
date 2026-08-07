package com.example.appliancemgmt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppliancemgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppliancemgmtApplication.class, args);
	}

	@Bean
	CommandLineRunner debug(
			@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password) {

		return args -> {
			System.out.println("====================================");
			System.out.println("URL      : " + url);
			System.out.println("USERNAME : " + username);
			System.out.println("PASSWORD : " + password);
			System.out.println("====================================");
		};
	}
}