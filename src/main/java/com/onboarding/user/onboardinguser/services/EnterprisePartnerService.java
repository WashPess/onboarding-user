package com.onboarding.user.onboardinguser.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.models.EnterprisePartnerModel;
import com.onboarding.user.onboardinguser.repository.EnterprisePartnerRepository;
import com.onboarding.user.onboardinguser.utils.Response;


@Service
public class EnterprisePartnerService extends ExceptionHandleService  {

	private final EnterprisePartnerRepository repository;
	private final EnterpriseService enterpriseService;

	public EnterprisePartnerService(EnterprisePartnerRepository repository, EnterpriseService enterpriseService) {
		this.repository = repository;
		this.enterpriseService = enterpriseService;
	}

	public Response save(EnterprisePartnerModel enterprisePartner){
		try {

			String partnerUuid = enterprisePartner.getPartnerUuid();
			String enterpriseUuid = enterprisePartner.getEnterpriseUuid();

			// a empresa existir
			EnterpriseModel enterprise = this.enterpriseService.getByUuid(enterpriseUuid);
			if(enterprise == null) {
				this.logger.error("A empresa informada não existe na base de dados.");
				return Response.error(404, "ENP000", "A empresa informada não existe na base de dados.");
			}

			EnterprisePartnerModel enterpriseData = this.getRelationshipByUuids(partnerUuid, enterpriseUuid);
			if(enterpriseData != null) {
				this.logger.error("Esse sócio já está associado a essa empresa.");
				return Response.error(409, "ENP001", "Esse sócio já está associado a essa empresa.");
			}

			this.repository.save(enterprisePartner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return Response.error(422, "ENP002", "Base de processamento na base de dados.");
		}
    }

	@Modifying
	public Response update(EnterprisePartnerModel enterprisePartner){
		try {
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "EPS002", "Base de dados indisponivel no momento.");
		}
	}
	
	public EnterprisePartnerModel getRelationshipByUuids(String partnerUuid, String enterpriseUuid){
		try {
			return this.repository.getRelationshipByUuids(partnerUuid, enterpriseUuid);
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return null;
		}
		
	}

	public Response deleteByUuid(String uuid) {
		try {
			
			// Busca a empresa com base no uuid
			EnterprisePartnerModel enterprise = this.getRelationshipByUuids(uuid, uuid);
			if(enterprise == null) {
				return Response.error(404, "EPS003", "Empresa não encontrada.");
			}

			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar a empresa ", e);
			return Response.error(422, "EPS004", "Servidor indisponível no momento.");
		}
	}
}