package com.onboarding.user.onboardinguser.services;


import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.repository.PartnerRepository;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PartnerService extends ExceptionHandleService {
	
	private final PartnerRepository repository;

	PartnerService(PartnerRepository repository) {
		this.repository = repository;
	}

		public Response save(PartnerModel partner){
		try {

			this.repository.save(partner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ACS001", "Base de dados indisponivel no momento.");
		}
    }
}
