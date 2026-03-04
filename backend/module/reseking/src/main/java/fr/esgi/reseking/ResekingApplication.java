package fr.esgi.reseking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ResekingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResekingApplication.class, args);
	}

}
