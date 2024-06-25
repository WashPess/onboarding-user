package com.onboarding.user.onboardinguser.controllers; // name space


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.services.UserService;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;

// silence report 

@RestController // decorator | annotation - controllar o comportamento de classe
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class UserController extends ExceptionHandle {

	private final UserService service;

	UserController(UserService service) {
		this.service = service;
	}

	@PostMapping("/user")
    ResponseEntity<Response> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {
		try{
			
			Response validUser = user.valid();
			if(validUser != null) {
				this.logger.error("Erro de validação do usuário.");
				return Response.result(validUser);
			}

			if (bindingResult.hasErrors()) {
				String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
				String log = String.format("Erro de validação no spring validation. %s", message);
				this.logger.error(log);
				return Response.result(Response.error(400, "USC000", message));
			}

			// delega a regra de salva para a service
			Response resultSaved = this.service.save(user);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar usuário.");
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(201));
		} catch(Exception e) {
			this.logger.error("Erro ao tentar salvar usuário.");
			return Response.result(Response.error(500, "USC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@PutMapping("/user/{uuid}")
    ResponseEntity<Response> update(@RequestBody UserModel user, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "USC011", "É necessário informar o uuid."));
			}
			
			Response validEmail = user.validEmail();
			if(validEmail != null) {
				return Response.result(validEmail);
			}

			Response validLastName = user.validLastName();
			if(validLastName != null) {
				return Response.result(validLastName);
			}

			Response validFirstName = user.validFirstName();
			if(validFirstName != null) {
				return Response.result(validFirstName);
			}

			Response validNickname= user.validNickname();
			if(validNickname != null) {
				return Response.result(validNickname);
			}

			user.setUuid(uuidStr);

			// delega a regra de salva para a service
			Response resultSaved = this.service.update(user);
			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC010", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@GetMapping("/user/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			// cast de variavel 
			Long uid = Long.parseLong(String.valueOf(id));

			if(uid == 0) {
				return Response.result(Response.error(404, "USC002", "É necessário informar o id."));
			}

			UserModel user = this.service.getById(uid);
			if(user == null) {
				return Response.result(Response.error(404, "USC003", "Usuário não encontrado."));
			}

			return Response.result(Response.success(200, user));
		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC004", "Servidor indisponível no momento."));
		}
	}

	@GetMapping("/user/{uuid}")
    ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "USC005", "É necessário informar o uuid."));
			}

			UserModel user = this.service.getByUuid(uuidStr);
			if(user == null) {
				return Response.result(Response.error(404, "USC006", "Usuário não encontrado."));
			}

			return Response.result(Response.success(200, user));

		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC007", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/user")
    ResponseEntity<Response> showByDocument(@RequestParam(required = true) String document) {
		try{

			String doc = Document.pad(Document.clear(document));
			
			if(Str.Empty(doc)) {
				return Response.result(Response.error(400, "USC008", "O envio do documento é obrigatório."));
			}

			UserModel user = this.service.getByDocument(doc);

			if(user == null) {
				return Response.result(Response.error(404, "USC009", "Usuário não encontrado."));
			}

			return Response.result(Response.success(200, user));
		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "USC007", "Servidor indisponível no momento."));
		}
	}

	@GetMapping("/users")
    ResponseEntity<Response> list() {
		List<UserModel> users = this.service.getAll();
		return Response.result(Response.success(200, users));
	}
	
} 