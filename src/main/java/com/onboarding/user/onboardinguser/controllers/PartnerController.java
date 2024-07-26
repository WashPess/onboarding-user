package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.services.PartnerService;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

@RestController
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class PartnerController extends ExceptionHandle {
    
	public final PartnerService service;

	PartnerController(PartnerService service) {
		this.service = service;
	}

	@PostMapping("/partner")
	ResponseEntity<Response> create(@RequestBody PartnerModel partner, BindingResult bindingResult) {
		try {

			Response validPartner = partner.valid();
            if(validPartner != null) {
                this.logger.error("Erro de validação na criaçao de conta.", new Exception(validPartner.toString()));
                return Response.result(validPartner);
            }
 
            if(bindingResult.hasErrors()) {
                String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
                this.logger.error("Erro de validaçao na criaçâo de conta usando spring validation.", new Exception(message));
                return Response.result(Response.error(400, "PAT000", message));
            }

			Response resultSaved = this.service.save(partner);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar o sócio na base de dados.", new Exception(resultSaved.toString()));
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(201));
		} catch(Exception e) {
			this.logger.error("Erro ao tentar salvar o sócio.", e);
			return Response.result(Response.error(500, "PAC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@PutMapping("/partner/{uuid}")
    ResponseEntity<Response> update(@RequestBody PartnerModel partner, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PAC011", "É necessário informar o uuid."));
			}

			Response validDocument= partner.validDocument();
			if(validDocument != null) {
				return Response.result(validDocument);
			}

			Response validEmail= partner.validEmail();
			if(validEmail != null) {
				return Response.result(validEmail);
			}

			partner.setUuid(uuidStr);

			Response resultSaved = this.service.update(partner);
			if(resultSaved.isError()) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(204));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar atualizar o sócio. {}", message, e);
				return  Response.result(response);
			}

			this.logger.error("Erro ao tentar atualizar o sócio da conta por uuid. ", e);
			return Response.result(Response.error(500, "PTM012", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@GetMapping("/partner/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(400, "PTM013", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário envia um id válido");
				return Response.result(Response.error(400, "PTM014", "É necessário enviar um id."));
			}

			PartnerModel partner = this.service.getById(uid);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca um sócio.");
				return Response.result(Response.error(404, "PTM015", "O sócio não foi encontrado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar buscar um sócio. {}", message, e);
				return  Response.result(response);
			}
            
            this.logger.error("Erro ao tentar buscar um sócio por id. ", e);
			return Response.result(Response.error(500, "PTM016", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/partner/{uuid}")
	ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PTM017", "É necessário informar o uuid."));
			}

			PartnerModel partner = this.service.getByUuid(uuidStr);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca uma conta.");
				return Response.result(Response.error(404, "PTM018", "O Sócio não foi encontrado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar buscar um sócio. {}", message, e);
				return  Response.result(response);
			}
			
			this.logger.error("Erro ao tentar buscar um sócio por uuid. ", e);
			return Response.result(Response.error(500, "PTC019", "Servidor indisponível no momento."));
		}
	}

	@DeleteMapping("/partner/{uuid}")
	ResponseEntity<Response> delete(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "PTC018", "É necessário informar o uuid."));
			}

			// deleta o usuário
			Response result = this.service.deleteByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar deletar o usuário por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "PTC019", "Servidor indisponível no momento."));
		}
	}

	@PatchMapping("/partner/{uuid}")
	ResponseEntity<Response> restore(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "PTC020", "É necessário informar o uuid."));
			}

			// restaura o usuário
			Response result = this.service.restoreByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar restaurar o usuário por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "PTC021", "Servidor indisponível no momento."));
		}
	}

}