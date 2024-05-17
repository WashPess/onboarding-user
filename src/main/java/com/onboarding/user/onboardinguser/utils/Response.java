package com.onboarding.user.onboardinguser.utils;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Response {

	@JsonIgnore
	boolean ok;

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

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isOk() {
		return (this.status >= 200 && this.status <= 299);
	}

	public static final Response error(int status, String code, String message) {
		return new Response(status, code, message);
	}

	public static final Response success(int status) {
		return new Response(status);
	}

	public static final Response success(int status, Object data) {
		Response res = new Response(status);
		res.setData(data);
		return res;
	}

	public static ResponseEntity<Response> result(Response res)  {
		if(res == null) {
			return ResponseEntity.status(204).build();
		}
		return ResponseEntity.status(res.getStatus()).body(res);
	}

}