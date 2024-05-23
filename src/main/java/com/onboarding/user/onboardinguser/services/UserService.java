package com.onboarding.user.onboardinguser.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onboarding.user.onboardinguser.models.UserModel;
import com.onboarding.user.onboardinguser.repository.UserRepository;

@Service
public class UserService {

	@Autowired
 	private UserRepository repo;

	public void save(UserModel user){
		this.repo.save(user);
	}

	public List<UserModel> getLastName(String name){
		return this.repo.findByLastName(name);
	}



}