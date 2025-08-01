package com.frota_manager.inteligent_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InteligentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InteligentManagerApplication.class, args);
	}

}
