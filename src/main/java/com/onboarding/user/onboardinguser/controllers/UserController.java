package com.onboarding.user.onboardinguser.controllers; // name space

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.models.UserModel;

import jakarta.validation.Valid;

@RestController // decorator | annotation - controllar o comportamento de classe
public class UserController {

	@PostMapping("/user")
    ResponseEntity<String> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ObjectError error = bindingResult.getAllErrors().get(0);
			String errorMessage = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

		return ResponseEntity.ok("Usuário validado");
	}

	
}