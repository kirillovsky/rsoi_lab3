package com.bmstu.rsoi_lab3.servers.ships;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

@Configuration
@EnableAutoConfiguration
public class BackendShipsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendShipsApplication.class);

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "backend_ships");
		SpringApplication.run(BackendShipsApplication.class, args);
	}

}
