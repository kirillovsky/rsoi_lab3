package com.bmstu.rsoi_lab3.servers.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Configuration
@EnableAutoConfiguration
public class BackendUsersApplication {

	//private static final Logger log = LoggerFactory.getLogger(BackendUsersApplication.class);

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "backend_user");
		SpringApplication.run(BackendUsersApplication.class, args);
	}


}
