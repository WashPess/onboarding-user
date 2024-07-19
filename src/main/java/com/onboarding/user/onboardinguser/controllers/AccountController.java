package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.AccountModel;
import com.onboarding.user.onboardinguser.services.AccountService;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;


@RestController
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class AccountController extends ExceptionHandle {
    
	private final AccountService service;

	AccountController(AccountService service) {
		this.service = service;
	}

    @PostMapping("/account")
    ResponseEntity<Response> create(@Valid @RequestBody AccountModel account, BindingResult bindingResult) {
        try{

            Response validAccount = account.valid();
            if(validAccount != null) {
                this.logger.error("Erro de validação na criaçao de conta.", validAccount.toString());
                return Response.result(validAccount);
            }

            if(bindingResult.hasErrors()) {
                String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
                this.logger.error("Erro de validaçao na criaçâo de conta usando spring validation.", message);
                return Response.result(Response.error(400, "ACC000", message));
            }

            Response resultSaved = this.service.save(account);
			if(resultSaved != null) {
				this.logger.error("Erro ao tentar salvar a conta.", resultSaved.toString());
				return Response.result(resultSaved);
            }

            return Response.result(Response.success(201));
		} catch(Exception e) {
            this.logger.error("Erro ao tentar salvar usuário.", e);
            return Response.result(Response.error(500, "ACC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
        }   
    }

	@PutMapping("/account/{uuid}")
    ResponseEntity<Response> update(@RequestBody AccountModel account, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "ACC011", "É necessário informar o uuid."));
			}

			Response validDocument= account.validDocument();
			if(validDocument != null) {
				return Response.result(validDocument);
			}
			
			Response validNickname= account.validNickname();
			if(validNickname != null) {
				return Response.result(validNickname);
			}

			Response validRg= account.validRG();
			if(validRg != null) {
				return Response.result(validRg);
			}

			account.setUuid(uuidStr);

			// delega a regra de salva para a service
			Response resultSaved = this.service.update(account);
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

    @GetMapping("/account/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(404, "ACC002", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário envia um id válido");
				return Response.result(Response.error(404, "ACC003", "É necessário enviar um id."));
			}

			AccountModel account = this.service.getById(uid);
			if(account == null) {
				this.logger.error("Erro ao tentar busca uma conta.");
				return Response.result(Response.error(404, "ACC004", "O usuário não foi encontrado."));
			}

			return Response.result(Response.success(200, account));
		} catch(Exception e) {
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				String message = response.toString();
				this.logger.error("Erro ao tentar busca uma conta. {}", message, e);
				return  Response.result(response);
			}
            
            this.logger.error("Erro na busca da conta por id. ", e);
			return Response.result(Response.error(500, "ACC005", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/account/{uuid}")
    ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "ACC006", "É necessário informar o uuid."));
			}

			AccountModel user = this.service.getByUuid(uuidStr);
			if(user == null) {
				return Response.result(Response.error(404, "ACC006", "Usuário não encontrado."));
			}

			return Response.result(Response.success(200, user));

		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "ACC007", "Servidor indisponível no momento."));
		}
	}
	
	@GetMapping("/account")
    ResponseEntity<Response>showByDocument(@RequestParam(required = true) String document) {
		try{

			String doc = Document.pad(Document.clear(document));
			if(Str.Empty(doc)) {
				return Response.result(Response.error(400, "ACC008", "O envio do documento é obrigatório."));
			}
			
			AccountModel account = this.service.getByDocument(doc);

			if(account == null) {
				return Response.result(Response.error(404, "ACC009", "Usuário não encontrado."));
			}

			account.setDocument(Document.mask(account.getDocument()));

			return Response.result(Response.success(200, account));
		} catch(Exception e) {
			this.logger.error(e.toString());
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "ACC010", "Servidor indisponível no momento."));
		}
	}

	@DeleteMapping("/account/{id}")
    ResponseEntity<Response> deleteEnterprise(@PathVariable Long id) {
        try {
            boolean isDeleted = service.delete(id);
            if (!isDeleted) {
                return Response.result(Response.error(404, "ACC011", "Conta não encontrada."));
            }
            return Response.result(Response.success(200));
        } catch (Exception e) {
            this.logger.error("Erro ao tentar excluir a conta.", e);
            return Response.result(Response.error(500, "ACC012", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
        }
    }
}




