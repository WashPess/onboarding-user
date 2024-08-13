package com.onboarding.user.onboardinguser.services;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.dto.PartnerUuidWithEnterpriseUuidDTO;
import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.models.EnterprisePartnerModel;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.repository.EnterprisePartnerRepository;
import com.onboarding.user.onboardinguser.repository.EnterpriseRepository;
import com.onboarding.user.onboardinguser.repository.PartnerRepository;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
@SuppressWarnings("squid:S1192")
public class EnterprisePartnerService extends ExceptionHandleService  {

	private final EnterprisePartnerRepository repository;
	private final EnterpriseRepository enterpriseRepository;
	private final PartnerRepository partnerRepository;
	
	@PersistenceContext
	private EntityManager entityManager;


	public EnterprisePartnerService(EnterprisePartnerRepository repository, EnterpriseRepository enterpriseRepository, PartnerRepository partnerRepository) {
		this.repository = repository;
		this.enterpriseRepository = enterpriseRepository;
		this.partnerRepository = partnerRepository;
	}

	public Response save(EnterprisePartnerModel enterprisePartner) {
		try {

			String partnerUuid = enterprisePartner.getPartnerUuid();
			String enterpriseUuid = enterprisePartner.getEnterpriseUuid();

			// verifica se a empresa existe
			EnterpriseModel enterprise = this.getEnterpriseByUuid(enterpriseUuid);
			if(enterprise == null) {
				this.logger.error("A empresa informada não existe na base de dados.");
				return Response.error(404, "ENP000", "A empresa informada não existe na base de dados.");
			}

			// verifica se a empresa está desabilitada
			if(enterprise.getStatus() == Status.DISABLED) {
				this.logger.error("A empresa informada está desabilitada por tempo indeterminado.");
				return Response.error(423, "ENP007", "A empresa informada está desabilitada por tempo indeterminado.");
			}

			// verifica se o sócio existe
			PartnerModel partner = this.getPartnerByUuid(partnerUuid);
			if(partner == null) {
				this.logger.error("O sócio informado não existe na base de dados.");
				return Response.error(404, "ENP001", "O sócio informado não existe na base de dados.");
			}

			// verifica se o sócio está desabilitado
			if(partner.getStatus() == Status.DISABLED) {
				this.logger.error("O sócio informado está desabilitado por tempo indeterminado.");
				return Response.error(423, "ENP008", "O sócio informado está desabilitado por tempo indeterminado.");
			}

			// verifica se o sócio informado já está associado empresa informada
			EnterprisePartnerModel enterpriseData = this.getRelationshipByUuids(enterpriseUuid, partnerUuid);
			if(enterpriseData != null) {
				this.logger.error("Esse sócio já está associado a essa empresa.");
				return Response.error(409, "ENP002", "Esse sócio já está associado a essa empresa.");
			}

			// salva a relação entre empresa e sócio
			this.repository.save(enterprisePartner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return Response.error(422, "ENP003", "Base de processamento na base de dados.");
		}
    }

	public Response saveRelationshipEnterpriseAndPartner(PartnerUuidWithEnterpriseUuidDTO partnerAndEnterprise) {
		EnterprisePartnerModel enterpriseAndPartnerModel = new EnterprisePartnerModel();
		enterpriseAndPartnerModel.setPartnerUuid(partnerAndEnterprise.getPartnerUuid());
		enterpriseAndPartnerModel.setEnterpriseUuid(partnerAndEnterprise.getEnterpriseUuid());
		return this.save(enterpriseAndPartnerModel);
	}

	@Modifying
	public Response update(EnterprisePartnerModel enterprisePartner) {
		try {
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ENP004", "Base de dados indisponivel no momento.");
		}
	}

	@Modifying
	// deleta a relação entre empresa e sócio
	public Response delete(EnterprisePartnerModel enterprisePartner) {
		try {

			// verifica se a empresa existe
			EnterpriseModel enterprise = this.getEnterpriseByUuid(enterprisePartner.getEnterpriseUuid());
			if(enterprise == null) {
				this.logger.error("A empresa informada não existe na base de dados.");
				return Response.error(404, "ENP009", "A empresa informada não existe na base de dados.");
			}

			// verifica se a empresa está desabilitada
			if(enterprise.getStatus() == Status.DISABLED) {
				this.logger.error("A empresa informada está desabilitada por tempo indeterminado.");
				return Response.error(423, "ENP010", "A empresa informada está desabilitada por tempo indeterminado.");
			}

			// verifica se o sócio existe
			PartnerModel partner = this.getPartnerByUuid(enterprisePartner.getPartnerUuid());
			if(partner == null) {
				this.logger.error("O sócio informado não existe na base de dados.");
				return Response.error(404, "ENP011", "O sócio informado não existe na base de dados.");
			}

			// verifica se o sócio está desabilitado
			if(partner.getStatus() == Status.DISABLED) {
				this.logger.error("O sócio informado está desabilitado por tempo indeterminado.");
				return Response.error(423, "ENP012", "O sócio informado está desabilitado por tempo indeterminado.");
			}
			
			// Busca a relaçao empres<->sócio com base no uuid
			EnterprisePartnerModel enterpriseData = this.getRelationshipByUuids(enterprisePartner.getEnterpriseUuid(), enterprisePartner.getPartnerUuid());
			if(enterpriseData == null) {
				return Response.error(404, "ENP005", "A relação entre a empresa e o sócio não existe.");
			}

			this.repository.delete(enterpriseData);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar a empresa ", e);
			return Response.error(422, "ENP006", "Servidor indisponível no momento.");
		}
	}

	@Modifying
	public Response deleteRelationshipEnterpriseAndPartner(PartnerUuidWithEnterpriseUuidDTO partnerAndEnterprise) {
		EnterprisePartnerModel enterpriseAndPartnerModel = new EnterprisePartnerModel();
		enterpriseAndPartnerModel.setPartnerUuid(partnerAndEnterprise.getPartnerUuid());
		enterpriseAndPartnerModel.setEnterpriseUuid(partnerAndEnterprise.getEnterpriseUuid());
		return this.delete(enterpriseAndPartnerModel);
	}
	
	public EnterprisePartnerModel getRelationshipByUuids(String enterpriseUuid, String partnerUuid) {
		try {
			return this.repository.getRelationshipByUuids(partnerUuid, enterpriseUuid);
		} catch(Exception e) {
			this.logger.error("Erro de processamento na base de dados", e);
			return null;
		}
		
	}

	public EnterpriseModel getEnterpriseByUuid(String uuid) {
		return this.enterpriseRepository.getByUuid(uuid);
	}

	public PartnerModel getPartnerByUuid(String uuid) {
		return this.partnerRepository.getByUuid(uuid);
	}

	public List<String> getListEnterprisesUuidsByPartnerUuid(String uuid) {
		return entityManager.createQuery(
			"SELECT ep.enterpriseUuid FROM PartnerModel p INNER JOIN EnterprisePartnerModel ep ON p.uuid = ep.partnerUuid WHERE p.uuid = :uuid",
			String.class)
			.setParameter("uuid", uuid)
			.getResultList();
	}

	public List<String> getListPartnersUuidsByEnterpriseUuid(String uuid) {
		return entityManager.createQuery(
			"SELECT ep.partnerUuid FROM EnterpriseModel e INNER JOIN EnterprisePartnerModel ep ON e.uuid = ep.enterpriseUuid WHERE e.uuid = :uuid",
			String.class)
			.setParameter("uuid", uuid)
			.getResultList();
	}

}