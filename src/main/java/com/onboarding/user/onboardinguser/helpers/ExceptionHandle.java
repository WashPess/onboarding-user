package com.onboarding.user.onboardinguser.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ExceptionHandle {

	protected final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response>  handleError(HttpServletRequest req, Exception ex) {
		
		String cause = String.format("Error: %s | raised : %s", req.getRequestURL(), ex);
		this.logger.error(cause);

		Response errorEnumMarital = ExceptionHandle.errorEnumMarital(ex);
		if(errorEnumMarital != null) {
			return Response.result(errorEnumMarital);
		}

		Response errorJsonParse = ExceptionHandle.errorJsonParse(ex);
		if(errorJsonParse != null) {
			return Response.result(errorJsonParse);
		}

		return Response.result(Response.error(500, "EXC000", "Servidor indisponível no momento. Volte mais tarde."));
	}

	public static final Response errorInput(Exception e) {
		String exStr = e.toString();
		if(exStr.contains("For input")) {
			return Response.error(400, "EXC001", "Erro nos parametros de entrada.");
		}
		return null;
	}


	public static final Response errorJsonParse(Exception e) {
		String exStr = e.toString();
		if(exStr.contains("JSON parse error")) {
			return Response.error(400, "EXC002", "A estrutura do objeto json não é compatível.");
		}
		return null;
	}

	public static final Response errorEnumMarital(Exception e) {
		String exStr = e.toString();
		if(exStr.contains("JSON parse error") && exStr.contains("enums.Marital")) {
			return Response.error(400, "EXC003", "O valor do campo marital está incompatível com o enum característico.");
		}
		return null;
	}

}