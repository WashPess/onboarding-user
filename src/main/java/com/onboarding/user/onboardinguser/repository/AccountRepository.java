package com.onboarding.user.onboardinguser.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.AccountModel;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<AccountModel, Long>, CrudRepository<AccountModel,Long> {
	
	public List<AccountModel> findAll();
	
	public AccountModel findById(long id);

	@Query("SELECT a FROM AccountModel a WHERE a.uuid = :uuid")
	public AccountModel getByUuid(@Param("uuid") String uuid);
	
	@Query("SELECT a FROM AccountModel a WHERE a.document = :doc")
	public AccountModel getByDocument(@Param("doc") String document);
	

}
