package com.onboarding.user.onboardinguser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@SpringBootApplication
public class OnboardingUserApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OnboardingUserApplication.class, args);
	}

}