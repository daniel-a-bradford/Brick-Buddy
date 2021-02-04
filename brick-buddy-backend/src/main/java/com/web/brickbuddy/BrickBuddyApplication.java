package com.web.brickbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Allows auto generated last modified timestamp
public class BrickBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrickBuddyApplication.class, args);
		
	}

}
