package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.domain.Sailors;
import com.bmstu.rsoi_lab3.service.SailorsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class BackendSailorsApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendSailorsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendSailorsApplication.class, args);
	}

}
