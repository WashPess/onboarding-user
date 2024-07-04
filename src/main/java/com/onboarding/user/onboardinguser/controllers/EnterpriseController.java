package com.onboarding.user.onboardinguser.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;

import com.onboarding.user.onboardinguser.services.EnterpriseService;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;

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

	@GetMapping("/enterprise/{id}")
    Object showById(@PathVariable Object id) {
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

			System.out.println(enterprise);

			// return Response.result(Response.success(200, enterprise));
			return ResponseEntity.status(200).body(enterprise);
			// return Response.result(Response.success(200));
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
	
}
