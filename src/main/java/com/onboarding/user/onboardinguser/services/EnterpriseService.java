package com.onboarding.user.onboardinguser.services;

import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.utils.Response;


@Service
public class EnterpriseService extends ExceptionHandleService  {

	public Response save(EnterpriseModel enterprise){
		try {

			System.out.println(enterprise);
			// this.repository.save(enterprise);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "EPS000", "Base de dados indisponivel no momento.");
		}
    }
    
}
