package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.PartnerModel;
import com.onboarding.user.onboardinguser.services.PartnerService;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;

@RestController
public class PartnerController extends ExceptionHandle {
    
	public final PartnerService service;

	PartnerController(PartnerService service) {
		this.service = service;
	}

	@PostMapping("/partner")
	ResponseEntity<Response> create(@Valid @RequestBody PartnerModel partner, BindingResult bindingResult) {
		
		Response resultSaved = this.service.save(partner);
		if(resultSaved != null) {
			this.logger.error("Erro ao tentar salvar a conta.", new Exception(resultSaved.toString()));
			return Response.result(resultSaved);
		}
		
		return null;
	}

	@PutMapping("/partner/{uuid}")
    ResponseEntity<Response> update(@RequestBody PartnerModel partner, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PAC011", "É necessário informar o uuid."));
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
			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "ACC012", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	@GetMapping("/partner/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(404, "PTM013", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário envia um id válido");
				return Response.result(Response.error(404, "PTM014", "É necessário enviar um id."));
			}

			PartnerModel partner = this.service.getById(uid);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca uma conta.");
				return Response.result(Response.error(404, "PTM015", "O usuário não foi encontrado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar busca uma conta. {}", message, e);
				return  Response.result(response);
			}
            
            this.logger.error("Erro na busca da conta por id. ", e);
			return Response.result(Response.error(500, "PTM016", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/partner/{uuid}")
	ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "PTM017", "É necessário informar o uuid."));
			}

			PartnerModel partner = this.service.getByUuid(uuidStr);
			if(partner == null) {
				this.logger.error("Erro ao tentar busca uma conta.");
				return Response.result(Response.error(404, "PTM018", "O Sócio não foi encontrado."));
			}

			return Response.result(Response.success(200, partner));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar busca uma conta. {}", message, e);
				return  Response.result(response);
			}
			
			this.logger.error("Erro na busca da conta por uuid. ", e);
			return Response.result(Response.error(500, "PTM019", "Servidor indisponível no momento."));
		}
	}

	@DeleteMapping("/partner/{id}")
    ResponseEntity<Response> deleteEnterprise(@PathVariable Long id) {
        try {
            boolean isDeleted = service.delete(id);
            if (!isDeleted) {
                return Response.result(Response.error(404, "PTM020", "Conta não encontrada."));
            }
            return Response.result(Response.success(200));
        } catch (Exception e) {
            this.logger.error("Erro ao tentar excluir a conta.", e);
            return Response.result(Response.error(500, "PTM021", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
        }
    }
}