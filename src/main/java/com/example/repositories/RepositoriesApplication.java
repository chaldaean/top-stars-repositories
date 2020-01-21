package com.example.repositories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class RepositoriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepositoriesApplication.class, args);
    }

	@Profile("!test")
    @Configuration
    @EnableScheduling
    public static class SpringConfiguration {
    }

}
