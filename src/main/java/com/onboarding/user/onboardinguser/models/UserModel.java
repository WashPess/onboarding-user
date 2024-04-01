package com.onboarding.user.onboardinguser.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// classe anêmica
public class UserModel {

	@NotNull
	@NotBlank(message = "O campo de email nome não pode ser vazio.") 
	// @Min(value=8, message = "O campo de email precisa ter no mínimo 8 caracteres.")
	// @Max(value=50, message = "O campo de email precisa ter no máximo 50 caracteres")
	@Pattern(regexp="['@']+", message="O campo email deve conter o caractere '@'")
	@Pattern(regexp="['.com']+", message="O campo email deve conter o caractere um domínio semelhante ao '.com' ou derivado")
	@Pattern(regexp="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}", message="O campo email deve conter uma email válido. 'joao@gmail.com'")
	// @Email(message = "O campo email deve conter um email válido")
	String email = "";
	
	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	String document = "";
	
	@NotNull
	@Size(min=2, max=30)
	@NotBlank(message = "O campo de último nome não pode ser vazio.")
	String lastName = "";

	@NotNull
	@Size(min=2, max=61)
	@NotBlank(message = "O campo de nome completo nome não pode ser vazio.")
	String fullName = "";

	@NotNull
	@Size(min=2, max=30)
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = "";

	// @Size(min=8, max=40)
	@NotBlank(message = "O campo de senha nome não pode ser vazio.")
	String password = "";

	@NotNull
	@Size(min=2, max=30)
	@NotBlank(message = "O campo de primeiro nome não pode ser vazio.")
	String firstName = "";

 	@AssertTrue
	boolean Optin = false; 			// aceite de termos

	@NotBlank(message = "O campo de confirmar senha nome não pode ser vazio.")
	String confirmPassword = "";

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDocument() {
		return this.document;
	}

    public  void setFirstName (String firstName) {  
        this.firstName = firstName;     
    }
    
    public String getFirstName() {
        return this.firstName;     
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;     
    }

    public void setFullName (String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;     
    }

	public void setNickName(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setOptin(boolean optin) {
		this.Optin = optin;
	}
	
	public boolean getOptin() {
		return this.Optin;
	}

}