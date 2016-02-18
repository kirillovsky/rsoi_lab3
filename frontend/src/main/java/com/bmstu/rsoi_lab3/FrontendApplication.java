package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.models.Sailors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
public class FrontendApplication {

	@Autowired
	BackendsConnector connector;

	private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

	@Bean
	public Converter<String, Sailors> sailorsConverter() {
		return new Converter<String, Sailors>() {
			@Override
			public Sailors convert(String id) {
				return connector.getSailor(Long.valueOf(id));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(UsersRepository repository) {
//		return (args) -> {
//			// save a couple of customers
////			repository.save(new Customer("Jack", "Bauer"));
////			repository.save(new Customer("Chloe", "O'Brian"));
////			repository.save(new Customer("Kim", "Bauer"));
////			repository.save(new Customer("David", "Palmer"));
////			repository.save(new Customer("Michelle", "Dessler"));
//
//			// fetch all customers
//
//			log.info("Users found with findAll(): " + repository.count());
//			log.info("-------------------------------");
//			for (Users customer : repository.findAll()) {
//				log.warn(customer.toString());
//			}
//			//repository.delete(new Long(6));
//			log.info("Users found with findAll(): " + repository.count());
//			log.info("Users found with findAll(): " + repository.findOne(100L));
//			//log.info("Users found with findAll(): " + repository.findAll(new PageRequest(0, 5)).getContent().size());
//			log.info("-------------------------------");
//
//			// fetch an individual customer by ID
////			Customer customer = repository.findOne(1L);
////			log.info("Customer found with findOne(1L):");
////			log.info("--------------------------------");
////			log.info(customer.toString());
////			log.info("");
////
////			// fetch customers by last name
////			log.info("Customer found with findByLastName('Bauer'):");
////			log.info("--------------------------------------------");
////			for (Customer bauer : repository.findByLastName("Bauer")) {
////				log.info(bauer.toString());
////			}
////			log.info("");
//		};
//	}
}
