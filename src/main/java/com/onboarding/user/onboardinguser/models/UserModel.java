package com.onboarding.user.onboardinguser.models;

import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.utils.Document;
import com.onboarding.user.onboardinguser.utils.PasswordHasher;
import com.onboarding.user.onboardinguser.utils.RegexCompile;
import com.onboarding.user.onboardinguser.utils.Response;
import com.onboarding.user.onboardinguser.utils.Str;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



@Entity
@DynamicUpdate
@Table(name = "users")
public class UserModel {

	@Transient 
	Logger logger = LoggerFactory.getLogger(UserModel.class);

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique=true)
	public String uuid;

	@NotNull
	@NotBlank(message = "O campo de email nome não pode ser vazio.") 
	@Size(min=8, max=40, message = "O email precisa ter no mínimo 8 e no máximo 40")
	@Pattern(regexp="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}", message="O campo email deve conter uma email válido. 'joao@gmail.com'")
	@Email(message = "O campo email deve conter um email válido")
	String email = "";
	
	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true)
	String document = ""; // CPF - 000.000.000-00 | CNPJ - 00.000.000/0000-00
	
	@NotNull
	@Size(min=2, max=30, message="O nome deve conter no mínimo 2 e no máximo 30 caracteres.")
	@NotBlank(message = "O campo de primeiro nome não pode ser vazio.")
	String firstName = "";

	@NotNull
	@NotBlank(message = "O campo de último nome não pode ser vazio.")
	@Size(min=2, max=30, message="O campo de último de conter no mínimo 2 caracterese no máximo 30 caracteres.")
	String lastName = "";

	String fullName = "";

	@NotNull
	@Size(min=2, max=30, message="O apelido deve conter no mínimo 2 e no máximo 30 caracteres.")
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = "";

	@NotBlank(message = "O campo de senha nome não pode ser vazio.")
	@Size(min=8, max=40, message="A senha deve conter no mínimo 8 caracteres e no máximo 40 caracteres.")
	@Pattern(regexp="(?=.*[\\d])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!*\\-\\._&+=])(?=\\S+$).{8,}", message="O campo de senha deve conter pelo menos uma letra maiúscula, uma minúscula, um caracteres especial, no mínimo 8 e no máximo 40 caracteres.")
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	String password = "";

 	@AssertTrue(message= "O campo de aceite deve ser marcado como verdadeiro.")
	boolean optin = false; 			// aceite de termos

	@NotBlank(message = "O campo de confirmar senha não pode ser vazio.")
	@JsonProperty( value = "confirmPassword", access = JsonProperty.Access.WRITE_ONLY)
	@Transient 
	String confirmPassword = "";
	
	Status status = Status.ENABLED;

	protected UserModel() {
	}

	public UserModel(String email, String document, String firsName, String lastName, String nickname, String password, String confirmPassword) {

		this.email = email;
		this.document = Document.pad(Document.clear(document));
		this.firstName = firsName;
		this.lastName = lastName;
		this.fullName = String.format("%s %s", this.firstName, this.lastName);
		this.nickname = nickname;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.optin = false;
		this.status = Status.ENABLED;

	}

	public String newUuid() {
		this.uuid = UUID.randomUUID().toString();
		return this.uuid;
	}

	public String passwordHash(){

		PasswordHasher hasher = new PasswordHasher();
		hasher.generateSalt();
		String salt = hasher.getSalt();

		this.password = hasher.hash(this.password, salt);

		return this.password;
	}

	public Long getId() {
		return  this.id;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setDocument(String document) {
		this.document = Document.pad(Document.clear(document));
	}

	public String getDocument() {
		return Document.mask(Document.clear(this.document));
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
		this.optin = optin;
	}
	
	public boolean getOptin() {
		return this.optin;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
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

	public Response validEmail() {

		if(this.email.length() == 0) {
			this.logger.error("O email não pode ser vazio");
			return Response.error(400, "USE001", "O email não pode ser vazio");
		}

		if(this.email.length() < 8) {
			String message = String.format("O email deve conter no mínimo 8 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "USE002", "O email deve conter no mínimo 8 caracteres");
		}

		if(this.email.length() > 40) {
			String message = String.format("O email deve conter no máximo 40 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "USE003", "O email deve conter no máximo 40 caracteres.");
		}

		if(!this.email.contains("@")) {
			String message = String.format("O email deve conter o caracter '@'. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "USE004", "O email deve conter o caracter '@'");
		}

		if(!this.email.contains(".")) {
			String message = String.format("O email deve conter um domínio válido. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "USE005", "O email deve conter um domínio válido");
		}
		
		if(RegexCompile.HasCharSpecialForEmail.matcher(this.email).find()) {
			String message = String.format("O email não pode ter caracter especial. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "USE006", "O email não pode ter caracter especial.");
		}

		return null;
	}

	private Response validDocument() {

		
		if(this.document.length() == 0) {
			this.logger.error("O documento não pode ser vazio");
			return Response.error(400, "USE007", "O documento não pode ser vazio.");
		}
		
		if(this.document.length() < 14) {
			String message = String.format("O documento deve conter no mínimo que 11 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "USE008", "O documento deve conter no mínimo 11 caracteres.");
		}

		if(this.document.length() > 19) {
			String message = String.format("O documento deve conter no mínimo que 11 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "USE009", "O documento deve conter no mínimo que 11 caracteres.");
		}

		if(RegexCompile.HasCharSpecialForDocument.matcher(this.document).find()) {
			String message = String.format("O documento não pode conter caracteres especiais. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "USE010", "O documento não pode conter caracteres especiais.");
		}

		if(!RegexCompile.OnlyNumberForDocument.matcher(this.document).find()) {
			String message = String.format("O documento deve conter somente numeros. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "USE011", "O documento deve conter somente numeros.");
		}

		return null;
	}

	public Response validFirstName() {

		if(this.firstName.length() == 0) {
			this.logger.error("O primeiro não pode ser vazio");
			return Response.error(400, "USE012", "O primeiro nome não pode ser vazio.");
		}

		if(this.firstName.length() < 2) {
			String message = String.format("O primeiro nome não pode ser menor que 2 caracteres. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "USE013", "O primeiro nome não pode ser menor que 2 caracteres.");

		}

		if(this.firstName.length() > 40) {
			String message = String.format("O primeiro nome não pode ser maior que 40 caracteres. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "USE014", "O primeiro nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.firstName).find()) {
			String message = String.format("O primeiro nome deve conter somente letras. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "USE015", "O primeiro nome deve conter somente letras.");
		}

		return null;
	}

	public Response validLastName() {

		if(this.lastName.length() == 0) {
			this.logger.error("O último não pode ser vazio");
			return Response.error(400, "USE016", "O último nome não pode ser vazio.");
		}

		if(this.lastName.length() < 2) {
			String message = String.format("O último nome não pode ser menor que 2 caracteres. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "USE017", "O último nome não pode ser menor que 2 caracteres.");
		}

		if(this.lastName.length() > 40) {
			String message = String.format("O último nome não pode ser maior que 40 caracteres. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "USE018", "O último nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.lastName).find()) {
			String message = String.format("O último nome deve conter somente letras. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "USE019", "O último nome deve conter somente letras.");
		}

		return null;
	}

	public Response validNickname() {

		if(this.nickname.length() == 0) {
			this.logger.error("O apelido não pode ser vazio.");
			return Response.error(400, "USE020", "O apelido não pode ser vazio.");
		}

		if(this.nickname.length() < 2) {
			String message = String.format("O apelido não pode ser menor que 2 caracteres. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "USE021", "O apelido não pode ser menor que 2 caracteres.");
		}

		if(this.nickname.length() > 10) {
			String message = String.format("O apelido não pode ser maior que 10 caracteres. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "USE022", "O apelido não pode ser maior que 10 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetterForNickName.matcher(this.nickname).find()) {
			String message = String.format("O apelido não pode conter caracteres especiais. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "USE023", "O apelido não pode conter caracteres especiais.");
		}

		return null;
	}

	private Response validPassword() {

		if(this.password.length() == 0) {
			this.logger.error("A senha não pode ser vazia");
			return Response.error(400, "USE024", "A senha não pode ser vazia.");
		}

		if(this.password.length() < 8) {
			this.logger.error("A senha não pode ser menor que 8 caracteres.");
			return Response.error(400, "USE025", "A senha não pode ser menor que 8 caracteres.");
		}

		if(this.password.length() > 60) {
			this.logger.error("A senha não pode ser maior que 60 caracteres.");
			return Response.error(400, "USE026", "A senha não pode ser maior que 60 caracteres.");
		}

		if(!RegexCompile.HasLetterUpperCase.matcher(this.password).matches()) {
			this.logger.error("A senha precisa ter pelo menos 1 caracteres maiúsculo.");
			return Response.error(400, "USE027", "A senha precisa ter pelo menos 1 caracteres maiúsculo.");
		}

		if(!RegexCompile.HasLetterLowerCase.matcher(this.password).matches()) {
			this.logger.error("A senha precisa ter pelo menos 1 caracteres minúsculo.");
			return Response.error(400, "USE028", "A senha precisa ter pelo menos 1 caracteres minúsculo.");
		}

		if(!RegexCompile.HasCharSpecialSimple.matcher(this.password).find()) {
			this.logger.error("A senha precisa ter pelo menos 1 caracteres especial.");
			return Response.error(400, "USE029", "A senha precisa ter pelo menos 1 caracteres especial.");
		}

		if(!RegexCompile.Password.matcher(this.password).matches()) {
			this.logger.error("A senha precisa ter mais de 8 caracteres, pelo menos 1 caractere maiúsculo, 1 minúsculo e um especial.");
			return Response.error(400, "USE030", "A senha precisa ter mais de 8 caracteres, pelo menos 1 caractere maiúsculo, 1 minúsculo e um especial.");
		}
		
		return null;
	}

	private Response validConfirmPassword() {

		if(this.confirmPassword.length() == 0)	{
			this.logger.error("A confirmação de senha não pode ser vazia");
			return Response.error(400, "USE031", "A confimação de senha não pode ser vazia.");
		}

		if(this.confirmPassword.length() < 2) {
			this.logger.error("A confimação de senha não pode ser menor que 8 caracteres.");
			return Response.error(400, "USE032", "A confimação de senha não pode ser menor que 8 caracteres.");
		}
		
		if(!this.confirmPassword.equals(this.password)) {
			this.logger.error("A confimação de senha não pode ser diferente da senha.");
			return Response.error(400, "USE033", "A confimação de senha não pode ser diferente da senha.");
		}

		return null;
	}

	public UserModel inject(UserModel user) {
		
		if(!Str.Empty(user.getUuid())) {
			this.uuid = user.getUuid();
		}

		if(!Str.Empty(user.getFirstName())) {
			this.firstName = user.getFirstName();
		}

		if(!Str.Empty(user.getLastName())) {
			this.lastName = user.getLastName();
		}

		return this;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, uuid=%s, email=%s, document=%s, firstName='%s', lastName='%s', fullName=%s, nickname=%s, password=%s, status=%s]", this.id, this.uuid, this.email, this.document, this.firstName, this.lastName, this.fullName, this.nickname, this.password, this.status);
	}
}

