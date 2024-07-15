package com.onboarding.user.onboardinguser.services;


import java.util.Optional;

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
			partner.newUuid();
			this.repository.save(partner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return Response.error(422, "PTM001", "Base de dados indisponivel no momento.");
		}
    }

	public Response update(PartnerModel partner){
	  	try {

			// busca um socio pelo uuid e verifica se ele existe
			PartnerModel partnerData = this.repository.getByUuid(partner.getUuid());
			if(partnerData == null) {
				this.logger.error("O sócio não existe na base de dados");
				return Response.error(404, "PTS002", "O sócio não existe na base de dados.");
			}

			partnerData.setDocument(partner.getDocument());
			partnerData.setFirstName(partner.getFirstName());
			partnerData.setLastName(partner.getLastName());
			partnerData.setEmail(partner.getEmail());

			PartnerModel partnerUpdate = this.repository.save(partnerData);
			
			return Response.success(200, partnerUpdate);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return Response.error(422, "PTS003", "Base de dados indisponivel no momento.");
		}

	}

	public PartnerModel getById(Long id){
		Optional<PartnerModel> partner = this.repository.findById(id);

		if(partner.isEmpty()) {
			return null;
		}

		return partner.get();
	}

	public PartnerModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
	}

	public boolean delete(Long id){
		try {
			Optional<PartnerModel> enterprise = this.repository.findById(id);
			if(enterprise.isEmpty()) {
				this.logger.error("A conta não existe na base de dados");
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


// CODIGO COUNBOY - 