package com.onboarding.user.onboardinguser.models;

import org.hibernate.annotations.DynamicUpdate;
import java.util.UUID;

import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum Gender {
	MALE, FEMALE, VOID;
}

enum Marital {
	MARRIED, SINGLE, WIDOWER, SEPARATE, DIVORCED, VOID;
}

enum Currency {
	USD, EUR, BRL, JPY, VOID; 
}

enum Language {
	PORTUGUESE, ENGLISH, SPANISH, FRENCH, VOID; 
}

enum Nationality {
	BRAZILIAN, AMERICAN, SPANISH, FRENCH, VOID;

}
	
@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "accounts")
public class AccountModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	public String uuid;
	
	public String newUuid() {
		this.uuid = UUID.randomUUID().toString();
		return this.uuid;
	}
	
	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true)
	String document = ""; // CPF - 000.000.000-00 | CNPJ - 00.000.000/0000-00
	
	@NotNull
	@Size(min=2, max=30, message="O apelido deve conter no mínimo 2 e no máximo 30 caracteres.")
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = ""; 							// apelido
	
	// @NotNull
	// @NotBlank(message = "O campo do estado civil não pode ser vazio.")
	// Marital marital = Marital.SINGLE; 			// estado civil 
		
	// @NotNull
	// @NotBlank(message = "O campo moeda não pode ser vazio.")
	// Currency currency= Currency.BRL;				// moeda
	
	// @NotNull
	// @NotBlank(message = "O campo de linguagem não pode ser vazio.")
	// Language language = Language.PORTUGUESE;						// Idioma
	
	@NotNull
	@NotBlank(message = "O campo do rg não pode ser vazio.")
	@Size(min=1, max=12, message = "O rg precisa ter 12 caracteres.")
	@Column(name="rg", unique=true)
	String rg = ""; // RG - 00.000.000-0
	

	// @NotNull
	// @NotBlank(message = "O campo de nacionalidade não pode ser vazio.")
	// Nationality nationality = Nationality.BRAZILIAN;
	
	@AssertTrue(message= "O campo de aceite deve ser marcado como verdadeiro.")
	boolean optin = false;           //aceite de termos (mesmo sem ler)
	
	// @NotNull
	// @NotBlank(message = "O campo de gênero não pode ser vazio.")
	// Gender gender = Gender.MALE;
	
	public Response valid() {
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
