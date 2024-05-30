package com.onboarding.user.onboardinguser.controllers; // name space


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.services.UserService;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.Valid;

@RestController // decorator | annotation - controllar o comportamento de classe
public class UserController {

	@Autowired
	UserService service = new UserService();

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/user")
    ResponseEntity<Response> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {
		try{
			
			Response validUser = user.valid();
			if(validUser != null) {
				return Response.result(validUser);
			}

			if (bindingResult.hasErrors()) {
				ObjectError error = bindingResult.getAllErrors().get(0);
				String message = error.getDefaultMessage();
				return Response.result(Response.error(400, "USC000", message));
			}

			user.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));
			user.setDocument(Document.pad(Document.clear(user.getDocument())));
			user.passwordHash();

			Response resultSaved = this.service.save(user);
			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(201));
		} catch(Exception e) {
			return Response.result(Response.error(500, "USC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@GetMapping("/user/{id}")
    ResponseEntity<Response> show(@PathVariable Long id) {
		Object user = this.service.getById(id);
		return Response.result(Response.success(200, user));
	}

	@GetMapping("/users")
    ResponseEntity<Response> list() {
		Object users = this.service.getAll();
		return Response.result(Response.success(200, users));
	}
	
}
