package com.onboarding.user.onboardinguser.controllers; // name space

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.Valid;

@RestController // decorator | annotation - controllar o comportamento de classe
public class UserController {

	@PostMapping("/user")
    ResponseEntity<Response> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {

		Response validUser = user.valid();
		if(validUser != null) {
			return Response.result(validUser);
		}

		// if (bindingResult.hasErrors()) {
		// 	ObjectError error = bindingResult.getAllErrors().get(0);
		// 	String errorMessage = error.getDefaultMessage();
        //     return ResponseEntity.badRequest().body(errorMessage);
        // }
	
		return Response.result(Response.success(201));
	
	}

	@GetMapping("/userbyuuid")
    ResponseEntity<Response> show(@RequestParam String uuid) {
	
		return Response.result(Response.success(200, "sucesso"));
	}

	
}