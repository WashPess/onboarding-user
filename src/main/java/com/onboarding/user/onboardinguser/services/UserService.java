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

			// // busca o usuário com base no documento
			// UserModel userData = this.getByUuid(user.getUuid());

			// if(userData == null) {
			// 	this.log.error("Usuário não encontrado.");
			// 	return Response.error(404, "USS006", "Usuário não encontrado.");
			// }

			// // caso o usuário esteja desabilitaos, retornar erro 423
			// if(userData.getStatus() == Status.DISABLED) {
			// 	this.log.error("Usuário desabilitado por tempo inderterminado.");
			// 	return Response.error(423, "USS007", "Usuário desabilitado por tempo inderterminado.");
			// }

			// // Prepara o usuário para salvar
			// userData.inject(user);
			// userData.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));

			// System.out.println(userData);

			// this.repository.save(userData);
			Long id = (long) 17;

			Optional<UserModel> us = this.repository.findById(id);
			if(us.isPresent()){
				us.get().setEmail("Email");
				this.repository.save(us.get());
			}

			return null;
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