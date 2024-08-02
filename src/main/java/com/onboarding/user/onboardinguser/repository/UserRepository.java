package com.onboarding.user.onboardinguser.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onboarding.user.onboardinguser.models.UserModel;

import jakarta.annotation.Nullable;

@Repository

// Interface de repositório para manipulação de usuários
public interface UserRepository extends CrudRepository<UserModel,Long> {
	
	// Método para buscar todos os usuários
	@SuppressWarnings("null")
	public List<UserModel> findAll();
	
	// Método para buscar um usuário pelo id
	@Nullable
	public UserModel findById(long id);
	
	// Método para buscar um usuário pelo uuid
	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.uuid = :uuid")
	public UserModel getByUuid(@Param("uuid") String uuid);

	// Método para buscar um usuário pelo email
	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.email = :email")
	public UserModel getByEmail(@Param("email") String email);

	// Método para buscar um usuário pelo documento
	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.document = :doc")
	public UserModel getByDocument(@Param("doc") String document);

	// Método para buscar um usuário pelo nickname
	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.nickname = :nick")
	public UserModel getByNickname(@Param("nick") String nickname);

	@Nullable
	@Query("SELECT u FROM UserModel u WHERE u.status = 'enabled'")
	public List<UserModel> getAllEnableds();

	@Nullable
	@Query(
		"SELECT u "  
		+ "FROM UserModel u "
		+ "WHERE u.fullName ILIKE %:term% " 
		+ "OR u.document ILIKE %:term% "
		+ "OR u.nickname ILIKE %:term% "
		+ "OR u.email ILIKE %:term% "
		+ "ORDER BY u.fullName ASC"
	)
	public List<UserModel> getByATerm(@Param("term") String term);

}