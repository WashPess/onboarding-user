package com.onboarding.user.onboardinguser.controllers; // name space


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandle;
import com.onboarding.user.onboardinguser.models.AccountModel;
import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.services.AccountService;
import com.onboarding.user.onboardinguser.services.UserService;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.validation.Valid;

// silence report
@RestController //controllar o comportamento de classe
@SuppressWarnings("squid:S1192")

// classe de controle de usuário
public class UserController extends ExceptionHandle {

	// injeção de dependência
	private final UserService service;
	private final AccountService serviceAccount;

	// construtor
	UserController(UserService service, AccountService serviceAccount) {
		this.service = service;
		this.serviceAccount = serviceAccount;
	}

	// cria um usuário
	@PostMapping("/user")
    ResponseEntity<Response> create(@Valid @RequestBody UserModel user, BindingResult bindingResult) {
		try{
			
			// valida o usuário
			Response validUser = user.valid();
			if(validUser != null) {
				this.logger.error("Erro de validação do usuário.");
				return Response.result(validUser);
			}

			// verifica se houve erro de validação
			if (bindingResult.hasErrors()) {
				String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
				String log = "Erro de validação no spring validation. %s".formatted(message);
				this.logger.error(log);
				return Response.result(Response.error(400, "USC000", message));
			}

			// pegar o cpf + a data de nascimento + O nome da mãe e consultar na receita federal
			// -- CPF regular ou irregular
			// -- certidão de óbito
			// -- antecedentes criminais

			// pegar endereço + cep e consultar no via cep
			// -- endereço
			// -- dado GIS

			// pegar o email e consulta no https://haveibeenpwned.com/
			// -- ultima utilização da caixa de email
			// -- se o email foi vazado
			// -- data de criaçao do email

			// pegar o nome + cpf + dados bancários e consultar no OFAC - (BS2)
			// -- se o nome está na lista de terroristas

			// pegar nome completo e CPF e passar num beaoru - https://idwall.co/pt-BR/
			// -- nome 
			// -- endereço 
			// -- emprestimos

			// KYC - know your custumer - conheça seu cliente
			// -- sites que frenquenta
			// -- principais contas digitais
			// -- dados do analytics
			// 	 -- tempo médio de uso de internet
			// 	 -- principais gostos
			// 	 -- principais comportamentos (viagens, negócios, serviços de terceiros como drogarias, mercados, roupas)



			// delega a regra de salva para a service
			Response resultUserSaved = this.service.save(user);
			if(resultUserSaved != null) {
				String message = "Erro ao tentar salvar usuário. %s".formatted(resultUserSaved.toString());
				this.logger.error(message);
				return Response.result(resultUserSaved);
			}

			// Cria um account para o usuário atraves da criação do User
			AccountModel account = new AccountModel();
			account.newUuid();
			account.setUserUuid(user.getUuid());

			// Salva a conta
			Response resultAccountSaved = this.serviceAccount.save(account);
			if(resultAccountSaved != null) {
				this.logger.error("Erro ao tentar salvar a conta.", new Exception(resultAccountSaved.toString()));
				return Response.result(resultAccountSaved);
			}
			
			// retorna o status de sucesso
			return Response.result(Response.success(201));
		} catch(Exception e) {
			this.logger.error("Erro ao tentar salvar usuário.", e);
			return Response.result(Response.error(500, "USC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	// atualiza um usuário
	@PutMapping("/user/{uuid}")
    ResponseEntity<Response> update(@RequestBody UserModel user, @PathVariable Object uuid) {
		try{

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				this.logger.error("É necessário informar o uuid");
				return Response.result(Response.error(404, "USC002", "É necessário informar o uuid."));
			}
			
			// valida o email do usuário
			Response validEmail = user.validEmail();
			if(validEmail != null) {
				return Response.result(validEmail);
			}

			// valida o ultimo nome do usuário
			Response validLastName = user.validLastName();
			if(validLastName != null) {
				return Response.result(validLastName);
			}

			// valida o primeiro nome do usuário
			Response validFirstName = user.validFirstName();
			if(validFirstName != null) {
				return Response.result(validFirstName);
			}

			// valida o nickname do usuário
			Response validNickname= user.validNickname();
			if(validNickname != null) {
				return Response.result(validNickname);
			}

			user.setUuid(uuidStr);

			// delega a regra de update para a service
			Response resultSaved = this.service.update(user);
			if(resultSaved != null) {
				return Response.result(resultSaved);
			}

			// retorna o status de sucesso
			return Response.result(Response.success(204));
		} catch(Exception e) {
			this.logger.error("Error ao tentar fazer o update do usuário", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC003", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		}
	}

	// busca um usuário por id
	@GetMapping("/user/find/{id}")
    ResponseEntity<Response> showById(@PathVariable Object id) {
		try {

			// verifica se o id é vazio
			String idStr = String.valueOf(id);
			if(Str.Empty(idStr)) {
				this.logger.error("É necessário informar o id");
				return Response.result(Response.error(400, "USC004", "É necessário informar o id."));
			}

			// cast de variavel 
			Long uid = Long.parseLong(idStr);
			if(uid == 0) {
                this.logger.error("É necesário enviar um id válido");
				return Response.result(Response.error(400, "USC005", "É necessário enviar um id."));
			}

			// busca o usuário por id
			UserModel user = this.service.getById(uid);
			if(user == null) {
				return Response.result(Response.error(404, "USC006", "Usuário não encontrado."));
			}

			// retorna status de sucesso
			return Response.result(Response.success(200, user));
		} catch(Exception e) {
			this.logger.error("Error ao tentar buscar o usuário por id.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC007", "Servidor indisponível no momento."));
		}
	}

	// busca um usuário por uuid
	@GetMapping("/user/{uuid}")
    ResponseEntity<Response> showByUuid(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "USC008", "É necessário informar o uuid."));
			}
			
			// busca o usuário por uuid
			UserModel user = this.service.getByUuid(uuidStr);
			if(user == null) {
				return Response.result(Response.error(404, "USC009", "Usuário não encontrado."));
			}

			// retorna status de sucesso
			return Response.result(Response.success(200, user));

		} catch(Exception e) {
			this.logger.error("Error ao tentar buscar usuário por uuid.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC010", "Servidor indisponível no momento."));
		}
	}
	
	// busca um usuário por documento
	@GetMapping("/user")
    ResponseEntity<Response> showByDocument(@RequestParam(required = true) String document) {
		try{

			// padroniza o documento
			String doc = Document.pad(Document.clear(document));
			
			// verifica se o documento é vazio
			if(Str.Empty(doc)) {
				return Response.result(Response.error(400, "USC011", "O envio do documento é obrigatório."));
			}

			// busca o usuário por documento
			UserModel user = this.service.getByDocument(doc);

			// verifica se o usuário foi encontrado
			if(user == null) {
				return Response.result(Response.error(404, "USC012", "Usuário não encontrado."));
			}

			// retorna status de sucesso
			return Response.result(Response.success(200, user));
		} catch(Exception e) {
			this.logger.error("Error ao buscar usuário por documento.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return Response.result(response);
			}
			return Response.result(Response.error(500, "USC013", "Servidor indisponível no momento."));
		}
	}

	// lista todos os usuários
	@GetMapping("/users")
    ResponseEntity<Response> list() {
		try {
			
			// busca todos os usuários
			List<UserModel> users = this.service.getAll();
			return Response.result(Response.success(200, users));
		} catch (Exception e) {
			this.logger.error("Error ao buscar listar usuários.", e);
			return Response.result(Response.error(500, "USC015", "Servidor indisponível no momento."));
		}
	}
	
	// deleta um usuário
	@DeleteMapping("/user/{uuid}")
	ResponseEntity<Response> delete(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "USC014", "É necessário informar o uuid."));
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
			return Response.result(Response.error(500, "USC016", "Servidor indisponível no momento."));
		}
	}

	// restaura um usuário
	@PatchMapping("/user/{uuid}")
	ResponseEntity<Response> restore(@PathVariable Object uuid) {
		try {

			// cast de variavel 
			String uuidStr = String.valueOf(uuid);

			// verifica se o uuid é vazio
			if(Str.Empty(uuidStr)) {
				return Response.result(Response.error(404, "USC017", "É necessário informar o uuid."));
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
			return Response.result(Response.error(500, "USC018", "Servidor indisponível no momento."));
		}
	}

	// busca um usuário por um  termo
	@GetMapping("/users/search")
    ResponseEntity<Response> listByATerm(@RequestParam(required = true) Object term) {
		try {
			String termSentence = String.valueOf(term);

			if(Str.Empty(termSentence)) {
				return Response.result(Response.error(400, "USC019", "É necessário informar termo de busca."));
			}

			
			
			List<UserModel> users = this.service.getByATerm(termSentence);
			return Response.result(Response.success(200, users));

		} catch(Exception e) {
			this.logger.error("Error ao tentar buscar usuário por um termo.", e);
			Response response = ExceptionHandle.errorInput(e);
			if(response != null) {
				return  Response.result(response);
			}
			return Response.result(Response.error(500, "USC020", "Servidor indisponível no momento."));
		}
	}
} 	