package com.onboarding.user.onboardinguser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;

@Nullable
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Response {

    @JsonIgnore
	@Nullable
	public Logger logger = LoggerFactory.getLogger(Response.class);

	@JsonIgnore
	boolean ok;

	@JsonIgnore
	boolean redirect;

	@JsonIgnore
	boolean error;

	@JsonProperty("status")
	@JsonIgnore
	int status; 

	@JsonProperty("message")
	String message;
	
	@JsonProperty("code")
	String code;
	
	@JsonProperty("data")
	Object data;

	public Response(int status) {
		this.status = status;
	}

	public Response(int status, String code) {
		this.status = status;
		this.code = code;
	}

	public Response(int status, String code, String message) {
		this.message = message;
		this.status = status;
		this.code = code;
	}

	public int getStatus() {
		return this.status;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isOk() {
		return (this.status >= 200 && this.status <= 299);
	}

	public boolean isRedirect() {
		return (this.status >= 300 && this.status <= 399);
	}

	public boolean isError() {
		return (this.status >= 400 && this.status <= 599);
	}

	public static final Response error(int status, String code, String message) {
		return new Response(status, code, message);
	}

	public static final Response success(int status) {
		Response res = new Response(status);
		res.setData(null);
		return res;
	}

	public static final Response success(int status, Object data) {
		Response res = new Response(status);
		res.setData(data);
		return res;
	}

	public static ResponseEntity<Response> result(Response res) {
		try {
			if(res == null) {
				return ResponseEntity.status(204).body(null);
			}

			if(res.data == null && res.message != null && res.message.isEmpty()) {
				return ResponseEntity.status(res.getStatus()).body(null);
			}

			return ResponseEntity.status(res.getStatus()).body(res);
		} catch (Exception e) {
			if (res != null) {
				res.logger.error("Erro ao tentar responder: ", e);
			}
			Response error = new Response(400, "RES000", "Erro ao tentar converter a estrutura de dados");
			return ResponseEntity.status(400).body(error);
		}
	}

	@Override
	public String toString() {
		return String.format("Erro! Código: %s , Mensagem: %s", this.code, this.message);
	}

}