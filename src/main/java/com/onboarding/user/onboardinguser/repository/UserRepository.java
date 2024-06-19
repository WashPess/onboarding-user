package com.onboarding.user.onboardinguser.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.UserModel;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, Long>, CrudRepository<UserModel,Long> {
	
	public List<UserModel> findAll();
	
	public UserModel findById(long id);
	
	@Query("SELECT u FROM UserModel u WHERE u.uuid = :uuid")
	public UserModel getByUuid(@Param("uuid") String uuid);

	@Query("SELECT u FROM UserModel u WHERE u.email = :email")
	public UserModel getByEmail(@Param("email") String email);

	@Query("SELECT u FROM UserModel u WHERE u.document = :doc")
	public UserModel getByDocument(@Param("doc") String document);

}
