package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;

@Service
public class UserService {
	
	@Autowired
 	private UserRepository repository;

	Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Transactional
	public Response save(UserModel user){
		try {

			/// o documento do usuário já existe? se existir devolver erro 
			// UserModel userData = this.getByDocument(user.getDocument());
			// if(userData.getDocument().length() == 14) {
			// 	return Response.error(409, "USS002", "O documento já existe na base de dados.");
			// }


			// o email do usuário já existe?, se existir, devolver error 409
			// UserModel userDataWithEmail = this.getByEmail(user.getEmail());

			// if(userDataWithEmail.getEmail().length() > 8) {
			// 	return Response.error(409, "USS003", "O email já existe na base de dados.");
			// }

			// o usuário esta habilitado? Se tiver desativado devolver error 423 (recruso travado)

			user.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));
			user.passwordHash();
			user.newUuid();
			
			this.repository.save(user);
			return null;
		} catch(Exception e) {
			this.log.error("Erro na base de dados", e);
			return Response.error(422, "USS001", "Base de dados indisponivel no momento.");
		}
	}

	@Transactional
	public UserModel getByDocument(String document){
		return this.repository.getByDocument(Document.pad(Document.clear(document)));
	}

	@Transactional
	public UserModel getByEmail(String email){
		return this.repository.getByEmail(email);
	}

	@Transactional
	public Object getById(Long id){
		Object user = this.repository.findById(id);
		return user;
	}

	@Transactional
	public List<UserModel> getAll(){
		
		Iterable<UserModel> usersIter = this.repository.findAll();
		
		List<UserModel> users = new ArrayList<UserModel>();
		
		usersIter.forEach((UserModel user)-> {
			users.add(user);
		});

		return users;
	}




}