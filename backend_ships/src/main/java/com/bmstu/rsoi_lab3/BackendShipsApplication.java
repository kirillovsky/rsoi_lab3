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

	@Bean
	public CommandLineRunner demo(ShipsRepository repository) {
		return (args) -> {
			// save a couple of customers
//			repository.save(new Customer("Jack", "Bauer"));
//			repository.save(new Customer("Chloe", "O'Brian"));
//			repository.save(new Customer("Kim", "Bauer"));
//			repository.save(new Customer("David", "Palmer"));
//			repository.save(new Customer("Michelle", "Dessler"));

			// fetch all customers

			log.info("Ships found with findAll(): " + repository.count());
			log.info("-------------------------------");
			for (Ships customer : repository.findAll()) {
				log.warn(customer.toString());
			}
			//repository.delete(new Long(6));
			log.info("Ships found with findAll(): " + repository.count());
			log.info("Ships found with findAll(): " + repository.findOne(100L));
			//log.info("Ships found with findAll(): " + repository.findAll(new PageRequest(0, 5)).getContent().size());
			log.info("-------------------------------");

			// fetch an individual customer by ID
//			Customer customer = repository.findOne(1L);
//			log.info("Customer found with findOne(1L):");
//			log.info("--------------------------------");
//			log.info(customer.toString());
//			log.info("");
//
//			// fetch customers by last name
//			log.info("Customer found with findByLastName('Bauer'):");
//			log.info("--------------------------------------------");
//			for (Customer bauer : repository.findByLastName("Bauer")) {
//				log.info(bauer.toString());
//			}
//			log.info("");
		};
	}
}
