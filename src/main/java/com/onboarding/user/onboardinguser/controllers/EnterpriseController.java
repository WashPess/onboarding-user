package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.services.EnterpriseService;
import com.onboarding.user.onboardinguser.utils.Response;

@RestController
public class EnterpriseController extends ExceptionHandle {

    private final EnterpriseService service;

	EnterpriseController(EnterpriseService service) {
		this.service = service;
	}

    @PostMapping("/enterprise")
    ResponseEntity<Response> create(@RequestBody EnterpriseModel enterprise) {
        try{

            Response resultSaved = this.service.save(enterprise);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar o empreendimento.", new Exception(resultSaved.toString()));
				return Response.result(resultSaved);
            }

            return Response.result(Response.success(201));
		} catch(Exception e) {
            this.logger.error("Erro ao tentar salvar o empreendimento.", e);
            return Response.result(Response.error(500, "COC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
        }   
    }
}
