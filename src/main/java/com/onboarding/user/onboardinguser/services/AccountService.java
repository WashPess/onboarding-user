package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.AccountModel;
import com.onboarding.user.onboardinguser.repository.AccountRepository;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

@Service
@Transactional
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
public class AccountService extends ExceptionHandleService {
	
	private final AccountRepository repository;

	AccountService(AccountRepository repository) {
		this.repository = repository;
	}

	public Response save(AccountModel account){
		try {

			if(Str.Empty(account.getUserUuid())) {
				this.logger.error("Erro ao tentar salvar a conta, o uuid do usuário é obrigatório");
				return Response.error(409, "ACS000", "A conta precisa de um usuário vinculado para ser salva.");
			}

			this.repository.save(account);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ACS001", "Base de dados indisponivel no momento.");
		}
    }

	
	public Response update(AccountModel account){
		try {

			if(Str.Empty(account.getUserUuid())) {
				this.logger.error("Erro ao tentar salvar a conta, o uuid do usuário é obrigatório");
				return Response.error(409, "ACS002", "A conta precisa de um usuário vinculado para ser salva.");
			}

			AccountModel accountDataWithUser = this.getByUserUuid(account.getUserUuid());
            if(accountDataWithUser == null) {
				this.logger.error("A conta para este usuário não existe na base de dados.");
				return Response.error(404, "ACS003", "A conta para este usuário não existe na base de dados.");
			}

			if(accountDataWithUser.getRg() != null || !Str.Empty(accountDataWithUser.getRg())) {
				this.logger.error("A conta para este usuário já existe na base de dados.");
				return Response.error(404, "ACS004", "A conta para este usuário já existe na base de dados.");
			}

			accountDataWithUser.setRg(account.getRg());
			accountDataWithUser.setGender(account.getGender());
			accountDataWithUser.setLanguage(account.getLanguage());
			accountDataWithUser.setMarital(account.getMarital());
			accountDataWithUser.setNationality(account.getNationality());
			accountDataWithUser.setCurrency(account.getCurrency());
			accountDataWithUser.setOptin(account.getOptin());

			Date updatedAt = new Date();
			accountDataWithUser.setUpdatedAt(updatedAt);

			this.repository.save(accountDataWithUser);
			return Response.success(200, accountDataWithUser);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ACS006", "Base de dados indisponivel no momento.");
		}

	}

	public AccountModel getById(Long id){
		Optional<AccountModel> account = this.repository.findById(id);

		if(account.isEmpty()) {
			return null;
		}

		return account.get();
	}

	public AccountModel getByUuid(String uuid){
		return this.repository.getByUuid(uuid);
	}

	public AccountModel getByUserUuid(String uuid){
		return this.repository.getByUserUuid(uuid);
	}

	public List<AccountModel> getAll(){
		Iterable<AccountModel> accountsIter = this.repository.findAll();
		List<AccountModel> accounts = new ArrayList<>(0);
		accountsIter.forEach(accounts::add);
		return accounts;
	}

	public boolean delete(Long id){
		try {
			Optional<AccountModel> enterprise = this.repository.findById(id);
			if(enterprise.isEmpty()) {
				this.logger.error("A conta não existe na base de dados");
				return false;
			}
			
			this.repository.deleteById(id);
			return true;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return false;
		}
	}

}
