package com.onboarding.user.onboardinguser.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.EnterprisePartnerModel;

import jakarta.annotation.Nullable;


@Repository

public interface EnterprisePartnerRepository extends CrudRepository<EnterprisePartnerModel,Long> {

	@Nullable
	@Query("SELECT e FROM EnterprisePartnerModel e WHERE e.partnerUuid = :partnerUuid AND e.enterpriseUuid = :enterpriseUuid")
	public EnterprisePartnerModel getRelationshipByUuids(@Param("partnerUuid") String partnerUuid, @Param("enterpriseUuid") String enterpriseUuid);
}
