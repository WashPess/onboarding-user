package com.onboarding.user.onboardinguser.models;

import com.onboarding.user.onboardinguser.utils.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum Gender {
	Male, Female, Void;
}

enum Marital {
	Married, Single, Widower, Separate, Divorced, Void;
}
	
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	public String uuid;

	@NotNull
	@NotBlank(message = "O campo de documento nome não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true)
	String document = ""; // CPF - 000.000.000-00 | CNPJ - 00.000.000/0000-00
	
	@NotNull
	@Size(min=2, max=30, message="O apelido deve conter no mínimo 2 e no máximo 30 caracteres.")
	@NotBlank(message = "O campo de apelido nome não pode ser vazio.")
	String nickname = ""; 						// apelido
	
	// @NotNull
	// @NotBlank(message = "O campo do estado civíl não pode ser vazio.")
	// Marital marital = Marital.Single; 			// estado civil 
	
	// Date birthday = new Date();					//
	// String currency= "";						// moeda
	// String language = "";						// linguagem
	// String rg = "";					
	// String cpf = "";
	// String nationality = "";		
	// String picturePart = "";
	// boolean optin = false;
	// Gender gender = Gender.Male;
    // Date createdAt = new Date();
    // Date updateAt = new Date();
	
	public Response valid() {
		return Response.success(204);
	}

	@Override
	public String toString() {
		return String.format("Account[id=%d, uuid=%s, document=%s, nickname=%s]", this.id, this.uuid, this.document, this.nickname);
	}
}


// 0. https://www.baeldung.com/intro-to-project-lombok

// 1. Criar a controller (criar json do postman [verbo POST] com base nas propriedades da classe)
// 2. Criar a validaçao do spring validation
// 3. criar a validação personalizada
// 4. Ajustar o enum do Gender (colocar o enum na pasta correta e criar a classo de converter)
// 5. criar a service do Account (apenas verivicar se o dado chegou nela)
// 6. criar repository (método save)
// 7. converter a classe para uma tabela SQL (avaliar os casos de constrain)
// 8. criar a tabela sql no postgres (usando dbeaver)
// 9. testar a crianção do dados com o fluxo completo (enviar um dado no postgres e visualizar no banco de dados)
