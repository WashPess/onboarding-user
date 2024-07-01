package com.onboarding.user.onboardinguser.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "enterprises")
public class EnterpriseModel {
    
    @Transient 
	Logger log = LoggerFactory.getLogger(EnterpriseModel.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotNull
	@NotBlank(message = "O campo site não pode ser vazio.")
	@Size(min=2, max=26, message = "O site precisa ter no mínimo 2 e no máximo 26")
    String site = "";

    @NotNull
	@NotBlank(message = "O nome da empresa não pode ser vazio.")
    String company = "";

    @NotNull
    String timezone = "";

    @NotNull
	@NotBlank(message = "O campo profissão não pode ser vazio.")
    String professional = "";

    @Override
    public String toString() {
        return String.format("Enterprise[id=%d, site=%s, company=%s, timezone=%s, professional=%s]", this.id, this.site, this.company, this.timezone, this.professional);

    }
}