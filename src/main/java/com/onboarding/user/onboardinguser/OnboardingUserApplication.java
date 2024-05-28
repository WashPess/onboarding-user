package com.onboarding.user.onboardinguser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@SpringBootApplication
public class OnboardingUserApplication {

	private static final Logger log = LoggerFactory.getLogger(OnboardingUserApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(OnboardingUserApplication.class, args);
	}

}