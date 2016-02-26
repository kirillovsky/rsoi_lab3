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

}
