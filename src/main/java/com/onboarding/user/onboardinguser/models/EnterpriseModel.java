package com.onboarding.user.onboardinguser.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Transient;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "enterprises")
public class EnterpriseModel {
    
    @Transient 
	Logger log = LoggerFactory.getLogger(EnterpriseModel.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    // @NotNull
	// @NotBlank(message = "O campo site não pode ser vazio.")
	// @Size(min=2, max=26, message = "O site precisa ter no mínimo 2 e no máximo 26")
    String site = "";

    // @NotNull
	// @NotBlank(message = "O nome da empresa não pode ser vazio.")
    String company = "";

    // @NotNull
    // @NotBlank(message = "O campo de região não pode ser vazio.")
    String timezone = "";

    // @NotNull
	// @NotBlank(message = "O campo profissão não pode ser vazio.")
    String professional = "";

    // @NotNull
    // @NotEmpty(message = "O campo canais de comunicação não pode ser vazio.")
    // String[] communicationChannel = {};

    @Override
    public String toString() {
        return String.format("Enterprise[id=%d, site=%s, company=%s, timezone=%s, professional=%s]", this.id, this.site, this.company, this.timezone, this.professional);
    }
}