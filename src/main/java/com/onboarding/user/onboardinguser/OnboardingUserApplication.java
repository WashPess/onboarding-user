package com.onboarding.user.onboardinguser;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;


@ComponentScan
@SpringBootApplication
public class OnboardingUserApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OnboardingUserApplication.class, args);
	}

}