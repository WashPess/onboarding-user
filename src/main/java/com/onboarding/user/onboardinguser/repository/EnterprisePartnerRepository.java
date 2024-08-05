package com.onboarding.user.onboardinguser.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.EnterprisePartnerModel;

@Repository

public interface EnterprisePartnerRepository extends CrudRepository<EnterprisePartnerModel,Long> {

}
