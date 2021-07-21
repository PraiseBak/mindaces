package com.mindaces.mindaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MindacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindacesApplication.class, args);
	}

}
