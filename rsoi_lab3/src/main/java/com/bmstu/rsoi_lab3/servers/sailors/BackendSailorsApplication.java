package com.bmstu.rsoi_lab3.servers.sailors;

import com.bmstu.rsoi_lab3.servers.sailors.service.SailorsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendSailorsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendSailorsApplication.class);

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "backend_sailors");
		SpringApplication.run(BackendSailorsApplication.class, args);
	}

}
