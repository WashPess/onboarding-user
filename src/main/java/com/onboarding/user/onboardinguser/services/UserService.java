package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;


@Service
@Transactional
public class UserService {
	
	@Autowired
 	private  UserRepository repository;

	Logger log = LoggerFactory.getLogger(UserService.class);

	public Response save(UserModel user){
		try {

			// busca o usuário com base no documento
			UserModel userData = this.getByDocument(user.getDocument());

			// caso o usuário esteja desabilitaos, retornar erro 423
			if(userData != null && userData.getStatus() == Status.DISABLED) {
				this.log.error("Usuário desabilitado por tempo inderterminado.");
				return Response.error(423, "USS001", "Usuário desabilitado por tempo inderterminado.");
			}

			// Caso o documento exista, retornar erro 409
			if(userData != null && !Str.Empty(userData.getDocument())) {
				this.log.error("O documento já existe na base de dados");
				return Response.error(409, "USS002", "O documento já existe na base de dados.");
			}

			// Caso o email exista, retornar erro 409
			UserModel userDataWithEmail = this.getByEmail(user.getEmail());

			// caso o usuário esteja desabilitaos, retornar erro 423
			if(userDataWithEmail != null && userDataWithEmail.getStatus() == Status.DISABLED) {
				this.log.error("Usuário desabilitado por tempo inderterminado.");
				return Response.error(423, "USS003", "Usuário desabilitado por tempo inderterminado.");
			}

			// caso o email exista, retornar erro 409
			if(userDataWithEmail != null && !Str.Empty(userDataWithEmail.getEmail())) {
				this.log.error("O email já existe na base de dados");
				return Response.error(409, "USS004", "O email já existe na base de dados.");
			}

			// Prepara o usuário para salvar
			user.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));
			user.passwordHash();
			user.newUuid();
			
			this.repository.save(user);
			return null;
		} catch(Exception e) {
			this.log.error("Erro na base de dados", e);
			return Response.error(422, "USS005", "Base de dados indisponivel no momento.");
		}
	}

	@Modifying
	public Response update(UserModel user){
		try {

			UserModel userData = this.repository.findById(user.getId()).get();
			userData.setFirstName(user.getFirstName());
			userData.setLastName(user.getLastName());
			userData.setEmail(user.getEmail());
			UserModel userUpdated = this.repository.save(userData);
			
			return Response.success(200, userUpdated);
		} catch(Exception e) {
			this.log.error("Erro na base de dados", e);
			return Response.error(422, "USS008", "Base de dados indisponivel no momento.");
		}
	}

	public UserModel getById(Long id){
		Optional<UserModel> user = this.repository.findById(id);

		if(user.isEmpty()) {
			return null;
		}

		return user.get();
	}

	public UserModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
	}

	public UserModel getByEmail(String email){
		return this.repository.getByEmail(email);
	}

	public UserModel getByDocument(String document){
		return this.repository.getByDocument(Document.pad(Document.clear(document)));
	}

	public List<UserModel> getAll(){
		
		Iterable<UserModel> usersIter = this.repository.findAll();
		
		List<UserModel> users = new ArrayList<UserModel>();
		
		usersIter.forEach((UserModel user)-> {
			users.add(user);
		});

		return users;
	}

}