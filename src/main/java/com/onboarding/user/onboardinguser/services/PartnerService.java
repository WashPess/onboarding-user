package com.onboarding.user.onboardinguser.services;


import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.repository.PartnerRepository;
import com.onboarding.user.onboardinguser.utils.Document;
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

			// se o email já existir, retornar uma error (409) de dado duplicado
			PartnerModel partnerData = this.repository.getByEmail(partner.getEmail());
			if(partnerData != null) {
				this.logger.error("O email do sócio ja existe na base de dados");
				return Response.error(409, "PTS000", "O email do sócio ja existe na base de dados.");
			}

			// se o documento já existir, retornar uma error (409) de dado duplicado
			partner.setDocument(Document.pad(Document.clear(partner.getDocument())));
			PartnerModel partnerDataWithDocument = this.repository.getByDocument(partner.getDocument());
			if(partnerDataWithDocument != null) {
				this.logger.error("O documento do sócio ja existe na base de dados");
				return Response.error(409, "PTS001", "O documento do sócio ja existe na base de dados.");
			}

			partner.newUuid();
			partner.setFullName(partner.getFirstName() + " " + partner.getLastName());

			this.repository.save(partner);
			return null;
		} catch(Exception e) {

			Response resp = ExceptionHandleService.duplicateValue(e);
			if(resp != null) {
				this.logger.error("Erro de dados duplicados no sócio", e);
				return Response.error(409, "PTS002", "Erro de valores duplicados do sócio na base da dados."); 
			}

			this.logger.error("Erro na base de dados ao tentar salvar o sócio.", e);
			return Response.error(422, "PTS003", "Base de dados indisponivel no momento.");
		} 
    }

	public Response update(PartnerModel partner){
	  	try {

			// busca um socio pelo uuid e verifica se ele existe
			PartnerModel partnerData = this.repository.getByUuid(partner.getUuid());
			if(partnerData == null) {
				this.logger.error("O sócio não existe na base de dados");
				return Response.error(404, "PTS004", "O sócio não existe na base de dados.");
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
			return Response.error(422, "PTS005", "Base de dados indisponivel no momento.");
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

	public PartnerModel getByEmail(String email){
		try {
			return this.repository.getByEmail(email);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados.", e);
			return null;
		}
	}

	public PartnerModel getByUuidEnabled(String uuid){
		return this.repository.getByUuidEnabled(uuid);
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

	// Método para deletar um usuário com base no uuid
	public Response deleteByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			PartnerModel partner = this.getByUuid(uuid);
			if(partner == null) {
				return Response.error(404, "PTS006", "O sócio não encontrado.");
			}

			if(partner.getStatus() == Status.DISABLED) {
				return Response.error(423, "PTS007", "O sócio está desabilitado por tempo indeterminado.");
			}

			// Desabilita o usuário
			partner.setStatus(Status.DISABLED);
			Date updatedAt = new Date();
			partner.setUpdatedAt(updatedAt);
			this.repository.save(partner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar o sócio: ", e);
			return Response.error(422, "PTS008", "Servidor indisponível no momento.");
		}
	}

	// Método para restaurar um usuário com base no uuid
	public Response restoreByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			PartnerModel partner = this.getByUuid(uuid);
			if(partner == null) {
				return Response.error(404, "PTS009", "Sócio não encontrado.");
			}

			if(partner.getStatus() == Status.ENABLED) {
				return Response.error(400, "PTS010", "Este Sócio já está ativo no sistema.");
			}

			// Habilita o usuário
			partner.setStatus(Status.ENABLED);
			
			// Atualiza a data de atualização
			Date updatedAt = new Date();
			partner.setUpdatedAt(updatedAt);

			// Salva o usuário
			this.repository.save(partner);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar o Sócio: ", e);
			return Response.error(422, "PTS011", "Servidor indisponível no momento.");
		}
	}

}