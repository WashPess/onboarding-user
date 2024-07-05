package com.onboarding.user.onboardinguser.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.repository.EnterpriseRepository;
import com.onboarding.user.onboardinguser.utils.Response;


@Service
public class EnterpriseService extends ExceptionHandleService  {

	private final EnterpriseRepository repository;

	EnterpriseService(EnterpriseRepository repository) {
		this.repository = repository;
	}

	public Response save(EnterpriseModel enterprise){
		try {
			this.repository.save(enterprise);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return Response.error(422, "EPS000", "Base de processamento na base de dados.");
		}
    }

	@Modifying
	public Response update(EnterpriseModel enterprise){
		try {

			Optional<EnterpriseModel> enterpriseOptional = this.repository.findById(enterprise.getId());
			if(enterpriseOptional.isEmpty()) {
				this.logger.error("A empresa não existe na base de dados");
				return Response.error(404, "EPS001", "A empresa não existe na base de dados.");
			}

			EnterpriseModel enterpriseData = enterpriseOptional.get();
			EnterpriseModel enterpriseUpdated = this.repository.save(enterpriseData);
			
			return Response.success(200, enterpriseUpdated);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "EPS002", "Base de dados indisponivel no momento.");
		}
	}
	
	public EnterpriseModel getById(Long id){
		try {
			Optional<EnterpriseModel> enterprise = this.repository.findById(id);

			if(enterprise.isEmpty()) {
				return null;
			}

			return enterprise.get();
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return null;
		}
	}
}