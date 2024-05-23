package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.UserModel;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, Long>, CrudRepository<UserModel,Long>  {
	List<UserModel> findByLastName(@Param("name") String lastName);
  	UserModel findById(long id);
}

