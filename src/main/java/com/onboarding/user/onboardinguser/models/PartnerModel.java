package com.onboarding.user.onboardinguser.models;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "partners")
public class PartnerModel {
    
    @JsonIgnore
	@Transient 
	Logger logger = LoggerFactory.getLogger(PartnerModel.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank(message = "O email do Sócio não pode ser vazio.")
    @Size(min=5, max=50, message="O email do Sócio deve conter no mínimo 5 e no máximo 50 caracteres.")
    String email = "";
    
    @NotNull
	@NotBlank(message = "O documento do Sócio não pode ser vazio.")
	@Size(min=14, max=18, message = "O documento precisa ter no mínimo 11 e no máximo 14")
	@Column(name="document", unique=true)
	String document = "";

    @NotBlank(message = "O nome do Sócio não pode ser vazio.")
	@Size(min=2, max=30, message="O nome do Sócio deve conter no mínimo 2 e no máximo 30 caracteres.")
	String firstName = "";

    @NotBlank(message = "O sobrenome do Sócio não pode ser vazio.")
    @Size(min=2, max=30, message="O sobrenome deve conter no mínimo 2 e no máximo 30 caracteres.")
    String lastName = "";

    @Column(unique = true)
    public String uuid;

    public PartnerModel(String email, String document, String firstName, String lastName) {
        this.email = email;
        this.document = document;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String newUuid() {
        this.uuid = UUID.randomUUID().toString();
        return this.uuid;
    }

    @Override
    public String toString() {
        return String.format("PartnerModel[id=%d, email='%s', document='%s', firstName='%s', lastName='%s']", id, email, document, firstName, lastName);
    }
}
