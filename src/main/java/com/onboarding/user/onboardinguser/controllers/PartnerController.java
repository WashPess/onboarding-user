package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.services.PartnerService;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.Valid;

@RestController
public class PartnerController extends ExceptionHandle {
    
	public final PartnerService service;

	PartnerController(PartnerService service) {
		this.service = service;
	}

	@PostMapping("/partner")
	ResponseEntity<Response> create(@Valid @RequestBody PartnerModel partner, BindingResult bindingResult) {
		
		Response resultSaved = this.service.save(partner);
		if(resultSaved != null) {
			this.logger.error("Erro ao tentar salvar a conta.", new Exception(resultSaved.toString()));
			return Response.result(resultSaved);
		}
		
		return null;
	}
}