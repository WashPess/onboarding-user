package com.onboarding.user.onboardinguser.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.UserModel;

import jakarta.annotation.Nullable;

@Repository
public interface UserRepository extends CrudRepository<UserModel,Long> {
	
	@SuppressWarnings("null")
	public List<UserModel> findAll();
	
	@Nullable
	public UserModel findById(long id);
	
	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.uuid = :uuid")
	public UserModel getByUuid(@Param("uuid") String uuid);

	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.email = :email")
	public UserModel getByEmail(@Param("email") String email);

	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.document = :doc")
	public UserModel getByDocument(@Param("doc") String document);

}
