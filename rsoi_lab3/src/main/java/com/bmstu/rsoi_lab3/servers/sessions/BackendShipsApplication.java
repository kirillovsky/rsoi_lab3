package com.bmstu.rsoi_lab3.servers.sessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendShipsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendShipsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendShipsApplication.class, args);
	}

}
