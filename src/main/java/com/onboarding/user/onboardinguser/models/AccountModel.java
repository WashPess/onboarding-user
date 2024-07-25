package com.onboarding.user.onboardinguser.models;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onboarding.user.onboardinguser.enums.Currency;
import com.onboarding.user.onboardinguser.enums.Gender;
import com.onboarding.user.onboardinguser.enums.Language;
import com.onboarding.user.onboardinguser.enums.Marital;
import com.onboarding.user.onboardinguser.enums.Nationality;
import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountModel {

	@JsonIgnore
	@Transient 
	Logger logger = LoggerFactory.getLogger(AccountModel.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", columnDefinition = "BIGSERIAL PRIMARY KEY")
	@JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
	private Long id;
	
	@Column(name="uuid", unique = true, columnDefinition = "VARCHAR(64)")
	public String uuid;

    @Column(name="user_uuid", unique = true, columnDefinition = "VARCHAR(64)")
	public String userUuid;
	
	@NotNull(message = "O campo estado civil não pode ser vazio.")
	@Column(name="marital", columnDefinition = "VARCHAR(30)")
	Marital marital = Marital.SINGLE; // estado civil 
		
	@NotNull(message = "O campo moeda não pode ser vazio.")
	@Column(name="currency", columnDefinition = "VARCHAR(30)")
	Currency currency= Currency.BRL;				// moeda
	
	@NotNull(message = "O campo de idioma não pode ser vazio.")
	@Column(name="language", columnDefinition = "VARCHAR(30)")
	Language language = Language.PORTUGUESE;		// Idioma
	
	@NotNull(message = "O campo do rg não pode ser vazio.")
	@NotBlank(message = "O campo do rg não pode ser vazio.")
	@Size(min=1, max=12, message = "O rg precisa ter 12 caracteres.")
	@Column(name="rg", columnDefinition = "VARCHAR(30)")
	String rg = ""; // RG - 00.000.000-0 valid 
	
	@NotNull(message = "O campo de gênero não pode ser vazio.")
	@Column(name="gender", columnDefinition = "VARCHAR(30)")
	Gender gender = Gender.MALE;

	@NotNull(message = "O campo de nacionalidade não pode ser vazio.")
	@Column(name="nationality", columnDefinition = "VARCHAR(30)")
	Nationality nationality = Nationality.BRAZILIAN;

	@AssertTrue(message= "O campo de aceite deve ser marcado como verdadeiro.")
	@Column(name="optin", columnDefinition = "BOOLEAN DEFAULT FALSE")
	boolean optin = false;  //aceite de termos (mesmo sem ler)

	@Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date createdAt = new Date();

	@Column(name="updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	Date updatedAt = new Date();
	

	public String newUuid() {
		this.uuid = UUID.randomUUID().toString();
		return this.uuid;
	}

	public boolean getOptin() {
        return this.optin;
    }
	
	public Response valid() {


		Response validRG = this.validRG();
		if(validRG != null) {
			return validRG;
		}

		Response validMarital = this.validMarital();
		if(validMarital != null) {
			return validMarital;
		}

		Response validGender = this.validGender();
		if(validGender != null) {
			return validGender;
		}

		Response validLanguage = this.validLanguage();
		if(validLanguage != null) {
			return validLanguage;
		}

		Response validNationality = this.validNationality();
		if(validNationality != null) {
			return validNationality;
		}

		Response validCurrency = this.validCurrency();
		if(validCurrency != null) {
			return validCurrency;
		}

		return null;
	}
	

	public Response validRG() {
		
		if(this.rg.length() == 0) {
			this.logger.error("O rg não pode ser vazio");
			return Response.error(400, "ACM009", "O rg não pode ser vazio.");
		}
		
		if(this.rg.length() < 12) {
			String message = String.format("O rg deve conter no mínimo que 12 caracteres. %s", this.rg);
			this.logger.error(message);
			return Response.error(400, "ACM010", "O rg deve conter no mínimo 12 caracteres.");
		}

		if(this.rg.length() > 12) {
			String message = String.format("O rg deve conter no mínimo que 12 caracteres. %s", this.rg);
			this.logger.error(message);
			return Response.error(400, "ACM011", "O rg deve conter no mínimo que 12 caracteres.");
		}

		return null;
	}

	public Response validMarital() {
		
		if(this.marital == Marital.VOID) {
			this.logger.error("O estado civil não pode ser vazio.");
			return Response.error(400, "ACM012", "O estado civil não pode ser vazio.");
		}

		return null;
	}

	public Response validGender() {
		
		if(this.gender == Gender.VOID) {
			this.logger.error("O campo de gênero não pode ser vazio.");
			return Response.error(400, "ACM013", "O campo de gênero não pode ser vazio.");
		}

		return null;
	}

	public Response validLanguage() {
		
		if(this.language == Language.VOID) {
			this.logger.error("O campo de Linguagem não pode ser vazio.");
			return Response.error(400, "ACM014", "O campo de Linguagem não pode ser vazio.");
		}

		return null;
	}

	public Response validNationality() {
		
		if(this.nationality == Nationality.VOID) {
			this.logger.error("A nacionalidade não pode ser vazio.");
			return Response.error(400, "ACM015", "A nacionalidade não pode ser vazio.");
		}

		return null;
	}

	public Response validCurrency() {
		
		if(this.currency == Currency.VOID) {
			this.logger.error("O campo moeda não pode ser vazio.");
			return Response.error(400, "ACM016", "O campo moeda não pode ser vazio.");
		}

		return null;
	}

	@Override
	public String toString() {
		return String.format("Account[id=%d, uuid=%s, rg=%s]", this.id, this.uuid, this.rg);
	}
}


// 0. https://www.baeldung.com/intro-to-project-lombok

// 1. Criar a controller (criar json do postman [verbo POST] com base nas propriedades da classe)
// 2. Criar a validaçao do spring validation
// 3. criar a validação personalizada
// 4. Ajustar o enum do Gender (colocar o enum na pasta correta e criar a classo de converter)
// 5. criar a service do Account (apenas verificar se o dado chegou nela)
// 6. criar repository (método save)
// 7. converter a classe para uma tabela SQL (avaliar os casos de constrain)
// 8. criar a tabela sql no postgres (usando dbeaver)
// 9. testar a crianção do dados com o fluxo completo (enviar um dado no postgres e visualizar no banco de dados)
