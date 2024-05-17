package com.onboarding.user.onboardinguser.models;


import com.onboarding.user.onboardinguser.utils.RegexCompile;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


// classe anêmica
public class UserModel {

	@NotNull
	@NotBlank(message = "O campo de email nome não pode ser vazio.") 
	@Size(min=8, max=40, message = "O email precisa ter no mínimo 8 e no máximo 40")
	@Pattern(regexp="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}", message="O campo email deve conter uma email válido. 'joao@gmail.com'")
	@Email(message = "O campo email deve conter um email válido")
	String email = "";
	
	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	String document = ""; // CPF - 000.000.000-00 | CNPJ - 00.000.000/0000-00
	
	@NotNull
	@NotBlank(message = "O campo de último nome não pode ser vazio.")
	@Size(min=2, max=30, message="O campo de último de conter no mínimo 2 caracterese no máximo 30 caracteres.")
	@Pattern(regexp="^[^0-9@()!-*#$%¨&+=]*$", message="O campo de último nome não pode conter números ou símbolos.")
	String lastName = "";

	String fullName = "";

	@NotNull
	@Size(min=2, max=30)
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = "";

	// @Size(min=8, max=40)
	@NotBlank(message = "O campo de senha nome não pode ser vazio.")
	@Size(min=8, max=40)
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!*\\-\\._&+=])(?=\\S+$).{8,}", message="O campo de senha deve conter pelo menos uma letra maiúscula, uma minúscula, um caracteres especial, no mínimo 8 e no máximo 40 caracteres.")
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

//	public void setOptin(boolean optin) {
//		this.Optin = optin;
//	}
	
	public boolean getOptin() {
		return this.Optin;
	}

	public Response valid() {

		Response validEmail = this.validEmail();
		if(validEmail != null) {
			return validEmail;
		}

		Response validDoc = this.validDocument();
		if(validDoc != null) {
			return validDoc;
		}

		Response validFirstName = this.validFirstName();
		if(validFirstName != null) {
			return validFirstName;
		}

		Response validLastName = this.validLastName();
		if(validLastName != null) {
			return validLastName;
		}

		Response validNickname = this.validNickname();
		if(validNickname != null) {
			return validNickname;
		}

		Response validPassword = this.validPassword();
		if(validPassword != null) {
			return validPassword;
		}
		
		Response validConfirmPassword = this.validConfirmPassword();
		if(validConfirmPassword != null) {
			return validConfirmPassword;
		}
		
		return null;
	}

	private Response validEmail() {

		if(this.email.length() == 0) {
			return Response.error(400, "USE001", "O email não pode ser vazio");
		}

		if(this.email.length() < 8) {
			return Response.error(400, "USE002", "O email deve conter no mínimo 8 caracteres");
		}

		if(this.email.length() > 40) {
			return Response.error(400, "USE003", "O email deve conter no máximo 40 caracteres");
		}

		if(!this.email.contains("@")) {
			return Response.error(400, "USE004", "O email deve conter o caracter '@'");
		}

		if(!this.email.contains(".")) {
			return Response.error(400, "USE005", "O email deve conter um domínio válido");
		}
		
		if(RegexCompile.HasCharSpecialForEmail.matcher(this.email).find()) {
			return Response.error(400, "USE006", "O email não pode ter caracter especial.");
		}

		return null;
	}

	private Response validDocument() {

		if(this.document.length() < 14) {
			return Response.error(400, "USE007", "O documento deve conter no mínimo que 11 caracteres.");
		}

		if(this.document.length() > 19) {
			return Response.error(400, "USE008", "O documento deve conter no mínimo que 11 caracteres.");
		}

		if(RegexCompile.HasCharSpecialForDocument.matcher(this.document).find()) {
			return Response.error(400, "USE009", "O documento não pode conter caracteres especiais.");
		}

		if(!RegexCompile.OnlyNumberForDocument.matcher(this.document).find()) {
			return Response.error(400, "USE010", "O documento deve conter somente numeros.");
		}

		return null;
	}

	private Response validFirstName() {

		if(this.firstName.length() == 0) {
			return Response.error(400, "USE011", "O primeiro nome não pode ser vazio.");
		}

		if(this.firstName.length() < 2) {
			return Response.error(400, "USE012", "O primeiro nome não pode ser menor que 2 caracteres.");

		}

		if(this.firstName.length() > 40) {
			return Response.error(400, "USE013", "Oprimeiro nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.firstName).find()) {
			return Response.error(400, "USE014", "O primeiro nome deve conter somente letras.");
		}

		return null;
	}

	private Response validLastName() {

		if(this.lastName.length() == 0) {
			return Response.error(400, "USE015", "O último nome não pode ser vazio.");
		}

		if(this.lastName.length() < 2) {
			return Response.error(400, "USE016", "O último nome não pode ser menor que 2 caracteres.");
		}

		if(this.lastName.length() > 40) {
			return Response.error(400, "USE017", "O último nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.lastName).find()) {
			return Response.error(400, "USE018", "O último nome deve conter somente letras.");
		}

		return null;
	}

	private Response validNickname() {

		if(this.nickname.length() == 0) {
			return Response.error(400, "USE019", "O apelido não pode ser vazio.");
		}

		if(this.nickname.length() < 2) {
			return Response.error(400, "USE020", "O apelido não pode ser menor que 2 caracteres.");
		}

		if(this.nickname.length() > 10) {
			return Response.error(400, "USE021", "O apelido não pode ser maior que 10 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetterForNickName.matcher(this.nickname).find()) {
			return Response.error(400, "USE022", "O apelido não pode conter caracteres especiais.");
		}

		return null;
	}

	private Response validPassword() {

		if(this.password.length() == 0) {
			return Response.error(400, "USE023", "A senha não pode ser vazia.");
		}

		if(this.password.length() < 8) {
			return Response.error(400, "USE024", "A senha não pode ser menor que 8 caracteres.");
		}

		if(this.password.length() > 60) {
			return Response.error(400, "USE025", "A senha não pode ser maior que 60 caracteres.");
		}

		if(!RegexCompile.HasLetterUpperCase.matcher(this.password).matches()) {
			return Response.error(400, "USE026", "A senha precisa ter pelo menos 1 caracteres maiúsculo.");
		}

		if(!RegexCompile.HasLetterLowerCase.matcher(this.password).matches()) {
			return Response.error(400, "USE027", "A senha precisa ter pelo menos 1 caracteres minúsculo.");
		}

		if(!RegexCompile.HasCharSpecialSimple.matcher(this.password).find()) {
			return Response.error(400, "USE028", "A senha precisa ter pelo menos 1 caracteres especial.");
		}

		if(!RegexCompile.Password.matcher(this.password).matches()) {
			return Response.error(400, "USE029", "A senha precisa ter mais de 8 caracteres, pelo menos 1 caractere maiúsculo, 1 minúsculo e um especial.");
		}
		
		return null;
	}

	private Response validConfirmPassword() {

		if(this.confirmPassword.length() == 0) {
			return Response.error(400, "USE030", "A confimação de senha não pode ser vazia.");
		}

		if(this.confirmPassword.length() < 2) {
			return Response.error(400, "USE031", "A confimação de senha não pode ser menor que 8 caracteres.");
		}
		
		if(!this.confirmPassword.equals(this.password)) {
			return Response.error(400, "USE032", "A confimação de senha não pode ser diferente da senha.");
		}

		return null;
	}
}
