package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onboarding.user.onboardinguser.models.EnterpriseModel;

import jakarta.annotation.Nullable;

public interface EnterpriseRepository extends CrudRepository<EnterpriseModel,Long> {
    
    @SuppressWarnings("null")
	public List<EnterpriseModel> findAll();

	@Nullable	
	public EnterpriseModel findById(long id);
}
