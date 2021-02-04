package com.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages="com.web.brickbuddy.repository")
@EntityScan(basePackages="com.web.brickbuddy")
public class ApplicationConfig {
	
	//the only purpose of this class is to avoid using xml to set the configuration.

}
