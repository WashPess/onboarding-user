package com.onboarding.user.onboardinguser.models;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onboarding.user.onboardinguser.enums.Currency;
import com.onboarding.user.onboardinguser.enums.Gender;
import com.onboarding.user.onboardinguser.enums.Language;
import com.onboarding.user.onboardinguser.enums.Marital;
import com.onboarding.user.onboardinguser.enums.Nationality;
import com.onboarding.user.onboardinguser.utils.RegexCompile;
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
	private Long id;
	
	@Column(unique=true)
	public String uuid;
	
	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true)
	String document = "";                            // CPF - 000.000.000-00 | CNPJ - 00.000.000/0000-00
	
	@NotNull
	@Size(min=2, max=30, message="O apelido deve conter no mínimo 2 e no máximo 30 caracteres.")
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = ""; 							// apelido
	
	@NotNull
	Marital marital = Marital.SINGLE; 			    // estado civil 
		
	@NotNull
	Currency currency= Currency.BRL;				// moeda
	
	@NotNull
	Language language = Language.PORTUGUESE;		// Idioma
	
	@NotNull
	@NotBlank(message = "O campo do rg não pode ser vazio.")
	@Size(min=1, max=12, message = "O rg precisa ter 12 caracteres.")
	@Column(name="rg", unique=true)
	String rg = ""; // RG - 00.000.000-0 valid 
	
	@NotNull
	Gender gender = Gender.MALE;

	@NotNull
	Nationality nationality = Nationality.BRAZILIAN;
	
	@AssertTrue(message= "O campo de aceite deve ser marcado como verdadeiro.")
	boolean optin = false;           //aceite de termos (mesmo sem ler)

	public String newUuid() {
		this.uuid = UUID.randomUUID().toString();
		return this.uuid;
	}
	
	public Response valid() {

		Response validDocument = this.validDocument();
		if(validDocument != null) {
			return validDocument;
		}

		Response validNickname = this.validNickname();
		if(validNickname != null) {
			return validNickname;
		}

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
	
	public Response validDocument() {

		if(this.document.length() == 0) {
			this.logger.error("O documento não pode ser vazio");
			return Response.error(400, "ACM000", "O documento não pode ser vazio.");
		}
		
		if(this.document.length() < 11) {
			String message = String.format("O documento deve conter no mínimo que 11 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "ACM001", "O documento deve conter no mínimo 11 caracteres.");
		}

		if(this.document.length() > 18) {
			String message = String.format("O documento deve conter no máximo que 18 caracteres. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "ACM002", "O documento deve conter no máximo que 18 caracteres.");
		}

		if(RegexCompile.HasCharSpecialForDocument.matcher(this.document).find()) {
			String message = String.format("O documento não pode conter caracteres especiais. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "ACM003", "O documento não pode conter caracteres especiais.");
		}

		if(!RegexCompile.OnlyNumberForDocument.matcher(this.document).find()) {
			String message = String.format("O documento deve conter somente numeros. %s", this.document);
			this.logger.error(message);
			return Response.error(400, "ACM004", "O documento deve conter somente numeros.");
		}

		return null;
	}

	public Response validNickname() {

		if(this.nickname.length() == 0) {
			this.logger.error("O apelido não pode ser vazio.");
			return Response.error(400, "ACM005", "O apelido não pode ser vazio.");
		}

		if(this.nickname.length() < 2) {
			String message = String.format("O apelido não pode ser menor que 2 caracteres. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "ACM006", "O apelido não pode ser menor que 2 caracteres.");
		}

		if(this.nickname.length() > 10) {
			String message = String.format("O apelido não pode ser maior que 10 caracteres. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "ACM007", "O apelido não pode ser maior que 10 caracteres.");
		}
		
		if(!RegexCompile.OnlyLetterForNickName.matcher(this.nickname).find()) {
			String message = String.format("O apelido não pode conter caracteres especiais. %s", this.nickname);
			this.logger.error(message);
			return Response.error(400, "ACM008", "O apelido não pode conter caracteres especiais.");
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
		return String.format("Account[id=%d, uuid=%s, document=%s, nickname=%s, rg=%s]", this.id, this.uuid, this.document, this.nickname, this.rg);
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
