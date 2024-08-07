package com.onboarding.user.onboardinguser.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.EnterpriseModel;
import com.onboarding.user.onboardinguser.repository.EnterpriseRepository;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;


@Service
@Transactional
@SuppressWarnings("squid:S1192") // Ignorar a regra de não repetir literais
public class EnterpriseService extends ExceptionHandleService  {

	private final EnterpriseRepository repository;

	public EnterpriseService(EnterpriseRepository repository) {
		this.repository = repository;
	}

	public Response save(EnterpriseModel enterprise){
		try {

			EnterpriseModel enterpriseData = this.getByCnpj(enterprise.getCnpj());

			if(enterpriseData != null && enterpriseData.getStatus() == Status.DISABLED) {
				this.logger.error("Empresa desabilitada por tempo inderterminado.");
				return Response.error(423, "EPS007", "Empresa desabilitada por tempo inderterminado.");
			}

			if(enterpriseData != null) {
				this.logger.error("Esse CNPJ ja existe no banco de dados.");
				return Response.error(409, "EPS008", "Esse CNPJ ja existe no banco de dados.");
			}

		
			enterprise.newUuid();
			enterprise.setCnpj(Document.clear(enterprise.getCnpj()));
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

	@Modifying
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

	@Modifying
	public Response deleteByUuid(String uuid) {
		try {
			
			// Busca a empresa com base no uuid
			EnterpriseModel enterprise = this.getByUuid(uuid);
			if(enterprise == null) {
				return Response.error(404, "EPS003", "Empresa não encontrada.");
			}

			if(enterprise.getStatus() == Status.DISABLED) {
				return Response.error(423, "EPS009", "A empresa está desabilitado por tempo indeterminado.");
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

	@Modifying
	public Response restoreByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			EnterpriseModel enterprise = this.getByUuid(uuid);
			if(enterprise == null) {
				return Response.error(404, "EPS005", "Empresa não encontrada.");
			}

			if(enterprise.getStatus() == Status.ENABLED) {
				return Response.error(400, "EPS010", "Esta empresa já está ativa no sistema.");
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

	public EnterpriseModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
	}

	public EnterpriseModel getByCnpj(String cnpj){
		return this.repository.getByCnpj(Document.clear(cnpj));
	}

	public List<EnterpriseModel> getAll(){
		return this.repository.getAllEnableds();	
	}
	
}