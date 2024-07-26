package com.onboarding.user.onboardinguser.controllers;

import java.util.List;

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
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.services.EnterpriseService;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;

@SuppressWarnings("squid:S1192")
@RestController
public class EnterpriseController extends ExceptionHandle {

    private final EnterpriseService service;

	EnterpriseController(EnterpriseService service) {
		this.service = service;
	}

    @PostMapping("/enterprise")
    ResponseEntity<Response> create(@Valid @RequestBody EnterpriseModel enterprise, BindingResult bindingResult) {
        try{

			if (bindingResult.hasErrors()) {
				String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
				String log = String.format("Erro de validação no spring validation. %s", message);
				this.logger.error(log);
				return Response.result(Response.error(400, "EPC000", message));
			}

            Response resultSaved = this.service.save(enterprise);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar o empreendimento.", new Exception(resultSaved.toString()));
				return Response.result(resultSaved);
            }

            return Response.result(Response.success(201));
		} catch(Exception e) {
            this.logger.error("Erro ao tentar salvar o empreendimento.", e);
            return Response.result(Response.error(500, "EPC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
        }   
    }

	@PutMapping("/enterprise/{id}")
    ResponseEntity<Response> update(@RequestBody EnterpriseModel enterprise, @PathVariable Object id) {
		try{

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(400, "EPCXXX", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário enviar um id válido");
				return Response.result(Response.error(400, "EPCXXX", "É necessário enviar um id."));
			}
			
			Response resultSaved = this.service.update(enterprise);
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
			return Response.result(Response.error(500, "EPCXXX", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@GetMapping("/enterprise/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(400, "EPC002", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid <= 0) {
                this.logger.error("É necesário envia um id válido");
				return Response.result(Response.error(400, "EPC003", "É necessário enviar um id."));
			}

			EnterpriseModel enterprise = this.service.getById(uid);
			if(enterprise == null) {
				this.logger.error("Erro ao tentar busca a empresa.");
				return Response.result(Response.error(404, "EPC004", "A empresa não foi encontrada."));

			}

			return Response.result(Response.success(200, enterprise));

		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar buscar uma conta. {}", message, e);
				return  Response.result(response);
			}
            
            this.logger.error("Erro na busca da conta por id.", e);
			return Response.result(Response.error(500, "EPC005", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/enterprises")
    ResponseEntity<Response> list() {
		List<EnterpriseModel> enterprises = this.service.getAll();
		return Response.result(Response.success(200, enterprises));
	}

	// deleta a empresa
	@DeleteMapping("/enterprise/{uuid}")
	ResponseEntity<Response> delete(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "EPC006", "É necessário informar o uuid."));
			}

			// deleta a empresa
			Response result = this.service.deleteByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar deletar a empresa por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "EPC007", "Servidor indisponível no momento."));
		}
	}

	// restaura a empresa
	@PatchMapping("/enterprise/{uuid}")
	ResponseEntity<Response> restore(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "EPC008", "É necessário informar o uuid."));
			}

			// restaura o usuário
			Response result = this.service.restoreByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar restaurar a empresa por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "EPC009", "Servidor indisponível no momento."));
		}
	}

}
