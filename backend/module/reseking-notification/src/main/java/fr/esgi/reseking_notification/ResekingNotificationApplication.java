package fr.esgi.reseking_notification;

import fr.esgi.reseking_notification.service.EmailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResekingNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResekingNotificationApplication.class, args);
	}

	@Bean
	public CommandLineRunner sendOnStartup(EmailService emailService) {
		return args -> emailService.sendTestEmail();
	}

}
