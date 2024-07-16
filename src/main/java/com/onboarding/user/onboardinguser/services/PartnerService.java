package com.onboarding.user.onboardinguser.services;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.repository.PartnerRepository;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class PartnerService extends ExceptionHandleService {
	
	private final PartnerRepository repository;

	PartnerService(PartnerRepository repository) {
		this.repository = repository;
	}

	public Response save(PartnerModel partner){
		try {

			partner.newUuid();
			partner.setFullName(partner.getFirstName() + " " + partner.getLastName());

			this.repository.save(partner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return Response.error(422, "PTS001", "Base de dados indisponivel no momento.");
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

			partnerData.setFirstName(partner.getFirstName());
			partnerData.setDocument(partner.getDocument());
			partnerData.setLastName(partner.getLastName());
			partnerData.setEmail(partner.getEmail());
			partnerData.setPhone(partner.getPhone());

			PartnerModel partnerUpdate = this.repository.save(partnerData);
			
			return Response.success(200, partnerUpdate);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return Response.error(422, "PTS003", "Base de dados indisponivel no momento.");
		}
	}

	public PartnerModel getById(Long id){
		try {
			Optional<PartnerModel> partner = this.repository.findById(id);

			if(partner.isEmpty()) {
				return null;
			}

			return partner.get();

		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return null;
		}
	}

	public PartnerModel getByUuid(String uuid){
		try {
			return this.repository.getByUuid(uuid);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return null;
		}
	}

	public boolean delete(Long id){
		try {
			Optional<PartnerModel> enterprise = this.repository.findById(id);
			if(enterprise.isEmpty()) {
				this.logger.error("O Sócio não existe na base de dados");
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