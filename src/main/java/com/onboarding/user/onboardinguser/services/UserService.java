package com.onboarding.user.onboardinguser.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;


@Service
@Transactional
@SuppressWarnings("squid:S1192")

// Classe de serviço para manipulação de usuários
public class UserService extends ExceptionHandleService {
	
	// Repositório de usuários
 	private final UserRepository repository;

	// Construtor da classe
	UserService(UserRepository repository){
		this.repository = repository;
	}

	// Método para salvar um usuário
	public Response save(UserModel user){
		try {

			// busca o usuário com base no documento
			UserModel userData = this.getByDocument(user.getDocument());

			// caso o usuário esteja desabilitado, retornar erro 423
			if(userData != null && userData.getStatus() == Status.DISABLED) {
				this.logger.error("Usuário desabilitado por tempo inderterminado.");
				return Response.error(423, "USS001", "Usuário desabilitado por tempo inderterminado.");
			}

			// Caso o documento exista, retornar erro 409
			if(userData != null && !Str.Empty(userData.getDocument())) {
				this.logger.error("O documento já existe na base de dados.");
				return Response.error(409, "USS002", "O documento já existe na base de dados.");
			}

			// Caso o email exista, retornar erro 409
			UserModel userDataWithEmail = this.getByEmail(user.getEmail());

			// caso o usuário esteja desabilitaos, retornar erro 423
			if(userDataWithEmail != null && userDataWithEmail.getStatus() == Status.DISABLED) {
				this.logger.error("Usuário desabilitado por tempo inderterminado.");
				return Response.error(423, "USS003", "Usuário desabilitado por tempo inderterminado.");
			}

			// caso o email exista, retornar erro 409
			if(userDataWithEmail != null && !Str.Empty(userDataWithEmail.getEmail())) {
				this.logger.error("O email já existe na base de dados");
				return Response.error(409, "USS004", "O email já existe na base de dados.");
			}

			UserModel userDataWithNickname = this.getByNickname(user.getNickname());

			// caso o usuário esteja desabilitaos, retornar erro 423
			if(userDataWithNickname != null && userDataWithNickname.getStatus() == Status.DISABLED) {
				this.logger.error("Usuário desabilitado por tempo inderterminado.");
				return Response.error(423, "USS008", "Usuário desabilitado por tempo inderterminado.");
			}


			// caso o usuário esteja desabilitaos, retornar erro 423
			if(userDataWithNickname != null && !Str.Empty(userDataWithNickname.getNickname())) {
				this.logger.error("O apelido já existe na base de dados.");
				return Response.error(423, "USS009", "O apelido já existe na base de dados.");
			}

			// Prepara o usuário para salvar
			user.setFullName("%s %s".formatted(user.getFirstName(), user.getLastName()));
			user.passwordHash();
			user.newUuid();
			
			// Salva o usuário
			this.repository.save(user);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "USS005", "Base de dados indisponivel no momento.");
		}
	}

	// Método para atualizar um usuário
	public Response update(UserModel user){
		try {

			// Busca o usuário com base no uuid
			UserModel userData = this.getByUuid(user.getUuid());
			
			// Caso o usuário não exista, retornar erro 404
			if(userData == null) {
				this.logger.error("O usuário não foi encontrado");
				return Response.error(404, "USS006", "O usuário não foi encontrado.");
			}

			// Atualiza os dados do usuário
			userData.setFirstName(user.getFirstName());
			userData.setLastName(user.getLastName());
			userData.setEmail(user.getEmail());
			this.repository.save(userData);
			
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "USS007", "Base de dados indisponivel no momento.");
		}
	}
	
	// Método para deletar um usuário
	public boolean delete(Long id) {
		try {
			Optional<UserModel> user = this.repository.findById(id);
			if(user.isPresent()) {
				this.repository.deleteById(id);
				return true; // Usuário deletado
			} else {
				return false; // Usuário não encontrado
			}
		} catch(Exception e) {
			this.logger.error("Erro ao deletar usuário: ", e);
			return false; // Erro durante para deletar usuário
		}
	}
	
	// Método para deletar um usuário com base no uuid
	public Response deleteByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			UserModel user = this.getByUuid(uuid);
			if(user == null) {
				return Response.error(404, "USS010", "Usuário não encontrado.");
			}

			// Desabilita o usuário
			user.setStatus(Status.DISABLED);
			Date updatedAt = new Date();
			user.setUpdatedAt(updatedAt);
			this.repository.save(user);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar usuário: ", e);
			return Response.error(422, "USS011", "Servidor indisponível no momento.");
		}
	}

	// Método para restaurar um usuário com base no uuid
	public Response restoreByUuid(String uuid) {
		try {
			
			// Busca o usuário com base no uuid
			UserModel user = this.getByUuid(uuid);
			if(user == null) {
				return Response.error(404, "USS012", "Usuário não encontrado.");
			}

			// Habilita o usuário
			user.setStatus(Status.ENABLED);
			
			// Atualiza a data de atualização
			Date updatedAt = new Date();
			user.setUpdatedAt(updatedAt);

			// Salva o usuário
			this.repository.save(user);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro ao deletar usuário: ", e);
			return Response.error(422, "USS013", "Servidor indisponível no momento.");
		}
	}

	// Método para buscar um usuário com base no id
	public UserModel getById(Long id){
		Optional<UserModel> user = this.repository.findById(id);

		// Caso o usuário não exista, retornar nulo
		if(user.isEmpty()) {
			return null;
		}

		return user.get();
	}

	// Método para buscar um usuário com base no uuid
	public UserModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
	}

	// Método para buscar um usuário com base no email
	public UserModel getByEmail(String email){
		return this.repository.getByEmail(email);
	}

	// Método para buscar um usuário com base no nickname
	public UserModel getByNickname(String nickname){
		return this.repository.getByNickname(nickname);
	}

	// Método para buscar um usuário com base no documento
	public UserModel getByDocument(String document){
		return this.repository.getByDocument(Document.pad(Document.clear(document)));
	}

	// Método para buscar todos os usuários
	public List<UserModel> getAll(){
		return this.repository.getAllEnableds();
	}

	// Método para buscar um lista de usuário filtrando por uma termo
	public List<UserModel> getByATerm(String term){
		return this.repository.getByATerm(term);
	}

} 