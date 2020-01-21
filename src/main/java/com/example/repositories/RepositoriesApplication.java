package com.example.repositories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RepositoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepositoriesApplication.class, args);
	}

}
