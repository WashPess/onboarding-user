package com.onboarding.user.onboardinguser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.models.AccountModel;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.Valid;

@RestController
public class AccountController {
    
    @PostMapping("/account")
    ResponseEntity<Response> create(@Valid @RequestBody AccountModel account, BindingResult bindingResult) {
        try{

            System.out.println(account);

            // Response validAccount = account.valid();
            // if(validAccount != null) {
            //     return Response.result(validAccount);
            // }

            // if(bindingResult.hasErrors()) {
            //     String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            //     String log = String.format("Erro de validação no spring validation. %s", message);
            //     return Response.result(Response.error(400, "USC000", message));
            // }

            return Response.result(Response.success(201));
		} catch(Exception e) {
			return Response.result(Response.error(500, "ACC001", "Servidor indisponível no momento. Favor tentar novamente mais tarde."));
		
        }   
    }
}


