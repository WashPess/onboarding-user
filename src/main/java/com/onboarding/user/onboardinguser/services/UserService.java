package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;

@Service
public class UserService {

	
	@Autowired
 	private UserRepository repository;
	
	@Transactional
	public void save(UserModel user){
		this.repository.save(user);
	}

	@Transactional
	public List<UserModel> getLastName(String name){
		return this.repository.findByLastName(name);
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