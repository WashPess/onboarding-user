package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onboarding.user.onboardinguser.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long>  {
	List<UserModel> findByLastNameStartsWithIgnoreCase(String lastName);
}

