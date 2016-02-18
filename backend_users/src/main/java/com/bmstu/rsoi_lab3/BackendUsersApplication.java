package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.domain.Users;
import com.bmstu.rsoi_lab3.service.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendUsersApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendUsersApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendUsersApplication.class, args);
	}

}
