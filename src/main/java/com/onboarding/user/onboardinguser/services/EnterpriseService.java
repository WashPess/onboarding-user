package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.repository.EnterpriseRepository;
import com.onboarding.user.onboardinguser.utils.Response;


@Service
public class EnterpriseService extends ExceptionHandleService  {

	private final EnterpriseRepository repository;

	public EnterpriseService(EnterpriseRepository repository) {
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

	public Response deleteByUuid(String uuid) {
		try {
			
			// Busca a empresa com base no uuid
			EnterpriseModel enterprise = this.getByUuid(uuid);
			if(enterprise == null) {
				return Response.error(404, "EPS003", "Empresa não encontrada.");
			}

			// Desabilita a empresa
			enterprise.setStatus(Status.DISABLED);
			Date updatedAt = new Date();
			enterprise.setUpdatedAt(updatedAt);
			this.repository.save(enterprise);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar a empresa ", e);
			return Response.error(422, "EPS004", "Servidor indisponível no momento.");
		}
	}

	public Response restoreByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			EnterpriseModel enterprise = this.getByUuid(uuid);
			if(enterprise == null) {
				return Response.error(404, "EPS005", "Empresa não encontrada.");
			}

			// Habilita o usuário
			enterprise.setStatus(Status.ENABLED);
			
			// Atualiza a data de atualização
			Date updatedAt = new Date();
			enterprise.setUpdatedAt(updatedAt);

			// Salva o usuário
			this.repository.save(enterprise);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar a empresa ", e);
			return Response.error(422, "EPS006", "Servidor indisponível no momento.");
		}
	}

	public EnterpriseModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
		}
	

}