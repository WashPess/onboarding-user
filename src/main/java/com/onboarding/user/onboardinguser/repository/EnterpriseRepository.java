package com.onboarding.user.onboardinguser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.onboarding.user.onboardinguser.models.EnterpriseModel;

import jakarta.annotation.Nullable;

public interface EnterpriseRepository extends CrudRepository<EnterpriseModel, Long> {

	@SuppressWarnings("null")
	public List<EnterpriseModel> findAll();

	@Nullable
	public EnterpriseModel findById(long id);

	// Método para buscar um usuário pelo uuid
	@Nullable
	@Query("SELECT e FROM EnterpriseModel e WHERE e.uuid = :uuid")
	public EnterpriseModel getByUuid(@Param("uuid") String uuid);

	@Nullable
    @Query("SELECT e FROM EnterpriseModel e WHERE e.uuid = :uuid")
    EnterpriseModel getByUuidEnabled(@Param("uuid") String uuid);

	@Nullable
	@Query("SELECT e FROM EnterpriseModel e WHERE e.cnpj = :cnpj")
	public EnterpriseModel getByCnpj(@Param("cnpj") String cnpj);

	@Nullable
	@Query("SELECT e FROM EnterpriseModel e WHERE e.status = 'enabled'")
	public List<EnterpriseModel> getAllEnableds();
}
