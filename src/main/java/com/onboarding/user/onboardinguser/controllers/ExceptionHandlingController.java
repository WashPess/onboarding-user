
package com.onboarding.user.onboardinguser.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ExceptionHandlingController {
    @ResponseStatus(HttpStatus.NOT_FOUND) 
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handlNotFound() {
        // Nothing to do
    }
}