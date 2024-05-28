package com.onboarding.user.onboardinguser.controllers; // name space


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
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.Valid;

@RestController // decorator | annotation - controllar o comportamento de classe
public class UserController {

	@Autowired
	UserService service = new UserService();

	@PostMapping("/user")
    ResponseEntity<Response> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {

		Response validUser = user.valid();
		if(validUser != null) {
			return Response.result(validUser);
		}

		if (bindingResult.hasErrors()) {
			ObjectError error = bindingResult.getAllErrors().get(0);
			String message = error.getDefaultMessage();
            return Response.result(Response.error(400, "USE033", message));
        }

		user.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));

		this.service.save(user);

		return Response.result(Response.success(201));
	
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

// /user (POST) 	create
// /user (GET) 		Read
// /user (PUT) 		Update
// /user (Delete) 	Delete


// /user/{id}