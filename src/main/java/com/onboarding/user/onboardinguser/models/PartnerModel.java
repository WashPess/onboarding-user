package com.onboarding.user.onboardinguser.models;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onboarding.user.onboardinguser.enums.Marital;
import com.onboarding.user.onboardinguser.enums.Status;
import com.onboarding.user.onboardinguser.utils.RegexCompile;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SuppressWarnings("squid:S1192") // Desativa a regra java:S1192
@Table(name = "partners")
public class PartnerModel {
    
    @JsonIgnore
	@Transient 
	Logger logger = LoggerFactory.getLogger(PartnerModel.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", columnDefinition = "SERIAL PRIMARY KEY")
	private Long id;

    @Column(unique = true, columnDefinition = "VARCHAR(64)")
    public String uuid;

	@NotNull(message = "O email do Sócio não pode ser vazio.")
    @NotBlank(message = "O email do Sócio não pode ser vazio.")
    @Size(min=5, max=60, message="O email do Sócio deve conter no mínimo 5 e no máximo 60 caracteres.")
	@Column(name="email", unique=true, nullable = false, columnDefinition = "VARCHAR(60)")
	String email = "";
    
    @NotNull(message = "O documento do Sócio não pode ser vazio.")
	@NotBlank(message = "O documento do Sócio não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true, nullable = false, columnDefinition = "VARCHAR(14)")
	String document = "";

	@NotNull(message = "O primeiro nome do Sócio não pode ser vazio.")
    @NotBlank(message = "O nome do Sócio não pode ser vazio.")
	@Size(min=2, max=30, message="O nome do Sócio deve conter no mínimo 2 e no máximo 30 caracteres.")
	@Column(name="first_name", nullable = false, columnDefinition = "VARCHAR(30)")
	String firstName = "";

	@NotNull(message = "O sobrenome do Sócio não pode ser vazio.")
    @NotBlank(message = "O sobrenome do Sócio não pode ser vazio.")
    @Size(min=2, max=30, message="O sobrenome deve conter no mínimo 2 e no máximo 30 caracteres.")
	@Column(name="last_name", nullable = false, columnDefinition = "VARCHAR(30)")
    String lastName = "";

	@Column(name="full_name", columnDefinition = "VARCHAR(60)")
	String fullName = "";

	@NotNull(message = "O endereço do Sócio não pode ser vazio.")
	@NotBlank(message = "O endereço do Sócio não pode ser vazio.")
    @Size(min=2, max=150, message="O endereço do Sócio deve conter no mínimo 2 e no máximo 150 caracteres.")
	@Column(name="address", nullable = false, columnDefinition = "TEXT")
    String address = "";

	@NotNull(message = "O telefone do Sócio não pode ser vazio.")
	@NotBlank(message = "O telefone do Sócio não pode ser vazio.")
	@Size(min=8, max=15, message="O telefone do Sócio deve conter no mínimo 8 e no máximo 15 caracteres.")
	@Pattern(regexp = "^\\d+$", message = "O telefone do Sócio deve conter somente números.")
	@Column(name="phone", nullable = false, columnDefinition = "VARCHAR(15)")
	String phone = "";

	@NotNull
	@Column(columnDefinition = "VARCHAR(40) DEFAULT 'single'")
	Marital marital = Marital.SINGLE;

	@NotNull(message = "O status do Sócio não pode ser vazio.")
	@Column(columnDefinition = "VARCHAR(40) DEFAULT 'enabled'", nullable = false)
	Status status = Status.ENABLED;

	@Column(name="created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date createdAt = new Date();

	@Column(name="updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	Date updatedAt = new Date();

    public PartnerModel(String email, String document, String firstName, String lastName) {
        this.email = email;
        this.document = document;
        this.lastName = lastName;
        this.firstName = firstName;
		this.fullName = String.format("%s %s", this.firstName, this.lastName);
    }

    public String newUuid() {
        this.uuid = UUID.randomUUID().toString();
        return this.uuid;
    }

	public Response valid() {

		Response validEmail = this.validEmail();
		if(validEmail != null) {
			return validEmail;
		}

		Response validDocument = this.validDocument();
		if(validDocument != null) {
			return validDocument;
		}

		Response validFirstName = this.validFirstName();
		if(validFirstName != null) {
			return validFirstName;
		}

		Response validLastName = this.validLastName();
		if(validLastName != null) {
			return validLastName;
		}

		Response validAddress = this.validAddress();
		if(validAddress != null) {
			return validAddress;
		}

		Response validPhone = this.validPhone();
		if(validPhone != null) {
			return validPhone;
		}		

		Response validMarital = this.validMarital();
		if(validMarital != null) {
			return validMarital;
		}

		return null;
	}
	
    public Response validDocument() {

		if(this.document.length() == 0) {
			this.logger.error("O documento não pode ser vazio");
			return Response.error(400, "PAM000", "O documento não pode ser vazio.");
		}
		
		if(this.document.length() < 11) {
			String message = String.format("O documento deve conter no mínimo que 11 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "PTM001", "O documento deve conter no mínimo 11 caracteres.");
		}

		if(this.document.length() > 18) {
			String message = String.format("O documento deve conter no máximo que 18 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "PTM002", "O documento deve conter no máximo que 18 caracteres.");
		}

		if(RegexCompile.HasCharSpecialForDocument.matcher(this.document).find()) {
			String message = String.format("O documento não pode conter caracteres especiais. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "PTM003", "O documento não pode conter caracteres especiais.");
		}

		if(!RegexCompile.OnlyNumberForDocument.matcher(this.document).find()) {
			String message = String.format("O documento deve conter somente numeros. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "PTM004", "O documento deve conter somente numeros.");
		}
        
        return null;
	}

    public Response validEmail() {

		if(this.email.length() == 0) {
			this.logger.error("O email não pode ser vazio.");
			return Response.error(400, "PTM005", "O email não pode ser vazio.");
		}

		if(this.email.length() < 8) {
			String message = String.format("O email deve conter no mínimo 8 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM006", "O email deve conter no mínimo 8 caracteres.");
		}

		if(this.email.length() > 40) {
			String message = String.format("O email deve conter no máximo 40 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM007", "O email deve conter no máximo 40 caracteres.");
		}

		if(!this.email.contains("@")) {
			String message = String.format("O email deve conter o caracter '@'. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM008", "O email deve conter o caracter '@'");
		}

		if(!this.email.contains(".")) {
			String message = String.format("O email deve conter um domínio válido. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM009", "O email deve conter um domínio válido");
		}
		
		if(RegexCompile.HasCharSpecialForEmail.matcher(this.email).find()) {
			String message = String.format("O email não pode ter caracter especial. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM010", "O email não pode ter caracter especial.");
		}

		return null;
	}

    public Response validFirstName() {

		if(this.firstName.length() == 0) {
			this.logger.error("O primeiro não pode ser vazio");
			return Response.error(400, "PTM011", "O primeiro nome não pode ser vazio.");
		}

		if(this.firstName.length() < 2) {
			String message = String.format("O primeiro nome não pode ser menor que 2 caracteres. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "PTM012", "O primeiro nome não pode ser menor que 2 caracteres.");

		}

		if(this.firstName.length() > 40) {
			String message = String.format("O primeiro nome não pode ser maior que 40 caracteres. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "PTM013", "O primeiro nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.firstName).find()) {
			String message = String.format("O primeiro nome deve conter somente letras. %s", this.firstName);
			this.logger.error(message);
			return Response.error(400, "PTM014", "O primeiro nome deve conter somente letras.");
		}

		return null;
	}

	public Response validLastName() {

		if(this.lastName.length() == 0) {
			this.logger.error("O último não pode ser vazio");
			return Response.error(400, "PTM015", "O último nome não pode ser vazio.");
		}

		if(this.lastName.length() < 2) {
			String message = String.format("O último nome não pode ser menor que 2 caracteres. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "PTM016", "O último nome não pode ser menor que 2 caracteres.");
		}

		if(this.lastName.length() > 40) {
			String message = String.format("O último nome não pode ser maior que 40 caracteres. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "PTM017", "O último nome não pode ser maior que 40 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetter.matcher(this.lastName).find()) {
			String message = String.format("O último nome deve conter somente letras. %s", this.lastName);
			this.logger.error(message);
			return Response.error(400, "PTM018", "O último nome deve conter somente letras.");
		}

		return null;
	}

	public Response validAddress() {

		if(this.address.length() == 0) {
			this.logger.error("O endereço não pode ser vazio");
			return Response.error(400, "PTM019", "O endereço não pode ser vazio");
		}

		if(this.address.length() < 8) {
			String message = String.format("O endereço deve conter no mínimo 2 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM020", "O endereço deve conter no mínimo 8 caracteres");
		}

		if(this.address.length() > 60) {
			String message = String.format("O endereço deve conter no máximo 60 caracteres. %s", this.email);
			this.logger.error(message);
			return Response.error(400, "PTM020", "O endereço deve conter no máximo 60 caracteres.");
		}

		return null;
	}

	public Response validPhone() {

		if(this.phone.length() == 0) {
			this.logger.error("O telefone não pode ser vazio");
			return Response.error(400, "PTM021", "O telefone não pode ser vazio.");
		}

		if(this.phone.length() < 8) {
			String message = String.format("O telefone deve conter no mínimo 8 caracteres. %s", this.phone);
			this.logger.error(message);
			return Response.error(400, "PTM022", "O telefone deve conter no mínimo 8 caracteres.");
		}

		if(this.phone.length() > 20) {
			String message = String.format("O telefone deve conter no máximo 20 caracteres. %s", this.phone);
			this.logger.error(message);
			return Response.error(400, "PTM023", "O telefone deve conter no máximo 20 caracteres.");
		}

		if(!RegexCompile.OnlyNumberForPhone.matcher(this.phone).find()) {
			String message = String.format("O telefone deve conter somente números. %s", this.phone);
			this.logger.error(message);
			return Response.error(400, "PTM024", "O telefone deve conter somente números.");
		}

		return null;
	}

	public Response validMarital() {
		
		if(this.marital == Marital.VOID) {
			this.logger.error("O estado civil do Sócio não pode ser vazio.");
			return Response.error(400, "PTM021", "O estado civil do Sócio não pode ser vazio.");
		}

		return null;
	}

	@Override
    public String toString() {
        return String.format("PartnerModel[id=%d, email='%s', document='%s', firstName='%s', lastName='%s', fullName=%s, address=%s, phone=%s]", id, email, document, firstName, lastName, fullName, address, phone);
    }

}
