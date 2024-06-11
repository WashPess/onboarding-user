package com.onboarding.user.onboardinguser.controllers; // name space


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.services.UserService;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

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

			// delega a regra de salva para a service
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
    ResponseEntity<Response> showById(@PathVariable Long id) {

		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("password", "confirmPassword");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", simpleBeanPropertyFilter);

		UserModel user = this.service.getById(id);
		if(user == null) {
			return Response.result(Response.error(404, "USC002", "Usuário não encontrado."));
		}
	
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		mappingJacksonValue.setFilters(filterProvider);


		return Response.result(Response.success(200, user));
	}
	

	@GetMapping("/user")
    ResponseEntity<Response> showByDocument(@RequestParam(required = true) String document) {
		String doc = Document.pad(Document.clear(document));
		
		if(Str.Empty(doc)) {
			return Response.result(Response.error(400, "USC003", "O envio do documento é obrigatório."));
		}

		UserModel user = this.service.getByDocument(doc);

		if(user == null) {
			return Response.result(Response.error(404, "USC004", "Usuário não encontrado."));
		}

		return Response.result(Response.success(200, user));
	}

	@GetMapping("/users")
    ResponseEntity<Response> list() {
		List<UserModel> users = this.service.getAll();
		return Response.result(Response.success(200, users));
	}
	
}