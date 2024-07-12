package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.List;
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

			// para criar uma empresa é preciso no mínimo uma conta
			// se o documento do sócio for uma conta bloqueada, não adicionar 
			// os sócios que são donos da empresa não precisam ser usuários do sistema	

			// TODO: fazer crud de sócios da empresa
			// levantar as propriedades da model
			// fazer o JSON
			// fazer sql do bnco de dados
			
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

	public List<EnterpriseModel> getAll(){
		Iterable<EnterpriseModel> enterprisesIter = this.repository.findAll();
		List<EnterpriseModel> enterprises = new ArrayList<>(0);
		enterprisesIter.forEach(enterprises::add);
		return enterprises;
	}

	public boolean delete(Long id){
		try {
			Optional<EnterpriseModel> enterprise = this.repository.findById(id);
			if(enterprise.isEmpty()) {
				this.logger.error("A empresa não existe na base de dados");
				return false;
			}
			this.repository.deleteById(id);
			return true;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return false;
		}
	}
}