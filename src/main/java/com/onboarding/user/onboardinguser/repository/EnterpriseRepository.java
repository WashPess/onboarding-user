package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onboarding.user.onboardinguser.models.EnterpriseModel;

public interface EnterpriseRepository extends CrudRepository<EnterpriseModel,Long> {
    
    @SuppressWarnings("null")
	public List<EnterpriseModel> findAll();
}
