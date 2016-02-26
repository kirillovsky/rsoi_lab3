package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.domain.Ships;
import com.bmstu.rsoi_lab3.service.ShipsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendShipsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendShipsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendShipsApplication.class, args);
	}

}
