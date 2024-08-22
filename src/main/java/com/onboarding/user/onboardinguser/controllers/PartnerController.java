package com.onboarding.user.onboardinguser.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.dto.PartnerUuidWithEnterpriseUuidDTO;
import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.EnterprisePartnerModel;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.services.EnterprisePartnerService;
import com.onboarding.user.onboardinguser.services.PartnerService;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

@RestController
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class PartnerController extends ExceptionHandle {

	// Service de sócio e de empresa
	public final PartnerService service;
	private final EnterprisePartnerService enterprisePartnerService;

	// Construtor da classe
	PartnerController(PartnerService service, EnterprisePartnerService enterprisePartnerService) {
		this.service = service;
		this.enterprisePartnerService = enterprisePartnerService;
	}

	// Cria um novo sócio
	@PostMapping("/partner")
	ResponseEntity<Response> create(@RequestBody PartnerModel partner, BindingResult bindingResult) {
		try {

			// 1 - valida os campos de sócio
			Response validPartner = partner.valid();
            if(validPartner != null) {
                this.logger.error("Erro de validação na criaçao de conta.", new Exception(validPartner.toString()));
                return Response.result(validPartner);
            }
	
	 		// 2 - valida os campos de sócio usando o srpng validation
            if(bindingResult.hasErrors()) {
                String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
                this.logger.error("Erro de validaçao na criaçâo de conta usando spring validation.", new Exception(message));
                return Response.result(Response.error(400, "PTC000", message));
            }

			// 3 - verifica se a empresa passada existe na base de dados
			Object enterprise = this.enterprisePartnerService.getEnterpriseByUuid(partner.getEnterpriseUuid());
			if(enterprise == null) {
				this.logger.error("A empresa informada não existe na nossa base de dados.");
                return Response.result(Response.error(400, "PTC001", "A empresa informada não existe na nossa base de dados."));
			}

			// 4 - cria um sócio na base de dados
			Response resultSaved = this.service.save(partner);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar o sócio na base de dados.", new Exception(resultSaved.toString()));
				return Response.result(resultSaved);
			}

			// 5 - Cria Relacionamento entre o sócio e a empresa
			EnterprisePartnerModel enterprisePartnerAssociation = new EnterprisePartnerModel();
			enterprisePartnerAssociation.setEnterpriseUuid(partner.getEnterpriseUuid());
			enterprisePartnerAssociation.setPartnerUuid(partner.getUuid());
			
			Response resultAssoc = this.enterprisePartnerService.save(enterprisePartnerAssociation);
			if(resultAssoc != null) {
				this.logger.error("Erro ao tentar relacionar o sócio com uma empresa.", new Exception(resultAssoc.toString()));
				return Response.result(resultAssoc);
			}

			return Response.result(Response.success(201));
		} catch(Exception e) {
			this.logger.error("Erro ao tentar salvar o sócio.", e);
			return Response.result(Response.error(500, "PTC002", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	// Atualiza um sócio
	@PutMapping("/partner/{uuid}")
    ResponseEntity<Response> update(@RequestBody PartnerModel partner, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PTC003", "É necessário informar o uuid."));
			}

			Response validDocument= partner.validDocument();
			if(validDocument != null) {
				return Response.result(validDocument);
			}

			Response validEmail= partner.validEmail();
			if(validEmail != null) {
				return Response.result(validEmail);
			}

			partner.setUuid(uuidStr);

			Response resultSaved = this.service.update(partner);
			if(resultSaved.isError()) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(204));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar atualizar o sócio. {}", message, e);
				return  Response.result(response);
			}

			this.logger.error("Erro ao tentar atualizar o sócio da conta por uuid. ", e);
			return Response.result(Response.error(500, "PTC004", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	// Deleta um sócio
	@DeleteMapping("/partner/{uuid}")
	ResponseEntity<Response> delete(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "PTC013", "É necessário informar o uuid."));
			}

			// deleta o usuário
			Response result = this.service.deleteByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar deletar o usuário por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "PTC014", "Servidor indisponível no momento."));
		}
	}

	// Restaura um sócio
	@PatchMapping("/partner/{uuid}")
	ResponseEntity<Response> restore(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "PTC015", "É necessário informar o uuid."));
			}

			// restaura o usuário
			Response result = this.service.restoreByUuid(uuidStr);
			if(result != null) {
				return Response.result(result);
			}

			// retorna status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar restaurar o usuário por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "PTC016", "Servidor indisponível no momento."));
		}
	}

	// Busca um sócio por id
	@GetMapping("/partner/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(400, "PTC005", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário envia um id válido");
				return Response.result(Response.error(400, "PTC006", "É necessário enviar um id."));
			}

			PartnerModel partner = this.service.getById(uid);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca um sócio.");
				return Response.result(Response.error(404, "PTC007", "O sócio não foi encontrado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar buscar um sócio. {}", message, e);
				return  Response.result(response);
			}
            
            this.logger.error("Erro ao tentar buscar um sócio por id. ", e);
			return Response.result(Response.error(500, "PTC008", "Servidor indisponível no momento."));
		}
	}
	
	// Busca um sócio por uuid
	@GetMapping("/partner/{uuid}")
	ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PTC009", "É necessário informar o uuid."));
			}

			PartnerModel partner = this.service.getByUuidEnabled(uuidStr);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca uma conta.");
				return Response.result(Response.error(404, "PTC010", "O Sócio não foi encontrado."));
			}

			if(partner.getStatus() == Status.DISABLED) {
				this.logger.error("O usuário está desabilitado por tempo determinado.");
				return Response.result(Response.error(423, "PTC013", "O usuário está desabilitado por tempo determinado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar buscar um sócio. {}", message, e);
				return  Response.result(response);
			}
			
			this.logger.error("Erro ao tentar buscar um sócio por uuid. ", e);
			return Response.result(Response.error(500, "PTC011", "Servidor indisponível no momento."));
		}
	}

	// Lista todos os sócios
	@GetMapping("/partners")
    ResponseEntity<Response> list() {
		try {
			List<PartnerModel> partners = this.service.getAll();
			return Response.result(Response.success(200, partners));
		} catch(Exception e) {
			this.logger.error("Error ao tentar listar os sócios", e);
			return Response.result(Response.error(500, "PTC012", "Servidor indisponível no momento."));
		}
	}

	//adicionar para uma empresa nova ou ja existente
	@PostMapping("/partner/enterprise")
    ResponseEntity<Response> addToAEnterpriseByUuid(@RequestBody PartnerUuidWithEnterpriseUuidDTO partnerWithEnterprise) {
		try{

			if(partnerWithEnterprise == null) {
				this.logger.error("É necessário informar o uuid do sócio e da empresa.");
				return Response.result(Response.error(404, "PTC017", "É necessário informar o uuid do sócio e da empresa."));
			}

			if(!partnerWithEnterprise.isValid()) {
				this.logger.error("É necessário informar um uuid valido do sócio e da empresa.");
				return Response.result(Response.error(404, "PTC020", "É necessário informar um uuid valido do sócio e da empresa."));
			}

			Response resultSaved = this.enterprisePartnerService.saveRelationshipEnterpriseAndPartner(partnerWithEnterprise);

			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(201));
		} catch(Exception e) {

			this.logger.error("Erro ao tentar atualizar o sócio da conta por uuid. ", e);
			return Response.result(Response.error(500, "PTC004", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	//adicionar para uma empresa nova ou ja existente
	@DeleteMapping("/partner/enterprise")
    ResponseEntity<Response> deleteAEnterpriseByUuid(@RequestBody PartnerUuidWithEnterpriseUuidDTO partnerWithEnterprise) {
		try{

			if(partnerWithEnterprise == null || !partnerWithEnterprise.isValid()) {
				this.logger.error("É necessário informar o uuid do sócio e o uuid da empresa.");
				return Response.result(Response.error(404, "PTC018", "É necessário informar o uuid do sócio e o uuid da empresa."));
			}

			Response resultSaved = this.enterprisePartnerService.deleteRelationshipEnterpriseAndPartner(partnerWithEnterprise);
			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Erro ao tentar deletar a relaçao entre sócio e empresa.", e);
			return Response.result(Response.error(500, "PTC019", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}
}