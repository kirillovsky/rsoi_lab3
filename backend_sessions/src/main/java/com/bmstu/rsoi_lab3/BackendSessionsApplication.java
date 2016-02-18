package com.bmstu.rsoi_lab3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendSessionsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendSessionsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendSessionsApplication.class, args);
	}
}
