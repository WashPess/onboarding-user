package com.onboarding.user.onboardinguser.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class ExceptionHandleService {

	protected final Logger logger = LoggerFactory.getLogger(ExceptionHandleService.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response>  handleError(HttpServletRequest req, Exception ex) {
		String cause = String.format("Error: %s | raised : %s", req.getRequestURL(), ex);
		this.logger.error(cause);
		return Response.result(Response.error(500, "EXS000", "Servidor indisponível no momento. Volte mais tarde."));
	}

}