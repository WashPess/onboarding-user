package com.onboarding.user.onboardinguser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;


@ComponentScan
@SpringBootApplication
public class OnboardingUserApplication {

	private static final Logger log = LoggerFactory.getLogger(OnboardingUserApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(OnboardingUserApplication.class, args);
	}

	@Bean
  	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			// save a few customers
			repository.save(new UserModel("email@email.com", "000.000.000-00", "Joao", "Bauer", "chao", "Alterar@123", "Alterar@123",  true));
			repository.save(new UserModel("email@email.com", "000.000.000-00","Chloe", "O'Brian", "chao", "Alterar@123", "Alterar@123", true));
			repository.save(new UserModel("email@email.com", "000.000.000-00", "Kim", "Bauer", "chao", "Alterar@123", "Alterar@123", true));
			repository.save(new UserModel("email@email.com", "000.000.000-00","David", "Palmer", "chao", "Alterar@123", "Alterar@123", true));
			repository.save(new UserModel("email@email.com", "000.000.000-00","Michelle", "Dessler", "chao", "Alterar@123", "Alterar@123", true));

			// fetch all customers
			log.info("Users found with findAll():");
			log.info("-------------------------------");
			repository.findAll().forEach(user -> {
				log.info(user.toString());
			});
			log.info("");

			// fetch an individual customer by ID
			UserModel user = repository.findById(1L);
			log.info("User found with findById(1L):");
			log.info("--------------------------------");
			log.info(user.toString());
			log.info("");

			// fetch customers by last name
			log.info("User found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			log.info("");

		};
	}

}