package com.onboarding.user.onboardinguser.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboarding.user.onboardinguser.helpers.ExceptionHandleService;
import com.onboarding.user.onboardinguser.models.AccountModel;
import com.onboarding.user.onboardinguser.repository.AccountRepository;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

@Service
@Transactional
public class AccountService extends ExceptionHandleService {
	
	private  final AccountRepository repository;

	AccountService(AccountRepository repository) {
		this.repository = repository;
	}

	public Response save(AccountModel account){
		try {

			account.setDocument(Document.pad(Document.clear(account.getDocument())));
			
            AccountModel accountData = this.getByDocument(account.getDocument());
            if(accountData != null && !Str.Empty(accountData.getDocument())) {
				this.logger.error("O documento já existe na base de dados");
				return Response.error(409, "ACS000", "O documento já existe na base de dados.");
			}
			
			account.newUuid();
			this.repository.save(account);
			return null;
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ACS001", "Base de dados indisponivel no momento.");
		}
    }

	@Modifying
	public Response update(AccountModel account){
		try {

			AccountModel accountData = this.repository.findById(account.getId()).get();
			AccountModel accountUpdate = this.repository.save(accountData);
			
			return Response.success(200, accountUpdate);
		} catch(Exception e) {
			this.logger.error("Erro na base de dados", e);
			return Response.error(422, "ACS002", "Base de dados indisponivel no momento.");
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

	public AccountModel getByDocument(String document){
		return this.repository.getByDocument(Document.pad(Document.clear(document)));
	}

	public List<AccountModel> getAll(){
		
		Iterable<AccountModel> accountsIter = this.repository.findAll();
		
		List<AccountModel> accounts = new ArrayList<AccountModel>();
		
		accountsIter.forEach((AccountModel account)-> {
			accounts.add(account);
		});

		return accounts;
	}

}

// dado many simple:  bit (o, 1)
// dado simple: byte: 8 bits () 
// char[] complex byte: 9-11 - uma cadeia de bytes | [ 000000000, 0000000000, 000000000 ]
// String 64(bits) char complex: uma cadeia de chars  [ 0: [000000000, 000000000,  ], 1: [000000000, 000000000] ]
// vertor: complex byte: cadeia de dados de um tipo defindo; vec<int> [ 1, 2, 3 ], vec<float> [ 1.23, 2.13, 3.12, ] | vec<char> [ a, b, c, 1]
// array: complex de many data: array<object>[ a, v, c, 1, asd]
// List:
// Collection: 
// Iterable:
// class: coleçao de dados de muitos tipos -> array: -> [ 1: array<object>, 2: str, 3: float, 4: int, 5> vec<int>, 6: List Colletion, Stream ]
// Stream: 
///
/// classe: é uma coleçao de dados de muitos tipos
