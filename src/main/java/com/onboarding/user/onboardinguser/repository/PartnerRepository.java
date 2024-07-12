package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.PartnerModel;

import jakarta.annotation.Nullable;

@Repository
public interface PartnerRepository extends PagingAndSortingRepository<PartnerModel, Long>, CrudRepository<PartnerModel,Long> {
	
	@SuppressWarnings("null")
	public List<PartnerModel> findAll();
	
	@Nullable	
	public PartnerModel findById(long id);

	@Nullable
	@Query("SELECT a FROM PartnerModel a WHERE a.uuid = :uuid")
	public PartnerModel getByUuid(@Param("uuid") String uuid);
	
	@Nullable
	@Query("SELECT a FROM PartnerModel a WHERE a.document = :doc")
	public PartnerModel getByDocument(@Param("doc") String document);
    
}
