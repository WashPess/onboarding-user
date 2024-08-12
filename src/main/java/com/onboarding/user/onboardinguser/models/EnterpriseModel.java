package com.onboarding.user.onboardinguser.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onboarding.user.onboardinguser.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    
    @JsonIgnore
    @Transient 
	Logger logger = LoggerFactory.getLogger(EnterpriseModel.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", columnDefinition = "BIGSERIAL PRIMARY KEY")
	@JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
	private Long id;

    @Column(name="uuid", unique = true, columnDefinition = "VARCHAR(64)")
	public String uuid;

    @NotNull(message = "O campo de região não pode ser vazio.")
    @NotBlank(message = "O campo de região não pode ser vazio.")
    @Column(name="timezone", nullable = false, columnDefinition = "VARCHAR(255)")
    String timezone = "";

    @NotNull(message = "O endereço da empresa não pode ser vazio.")
	@NotBlank(message = "O endereço da empresa não pode ser vazio.")
    @Size(min=2, max=150, message="O endereço da empresa deve conter no mínimo 2 e no máximo 150 caracteres.")
	@Column(name="address", nullable = false, columnDefinition = "TEXT")
    String address = "";

    @NotNull(message = "O endecampo cnpj da empresa não pode ser vazio.")
    @NotBlank(message = "O campo do cnpj não pode ser vazio.")
	@Size(min=14, max=18, message = "O cnpj precisa ter no mínimo 11 e no máximo 14 caracteres.")
	@Column(name="cnpj", unique=true, nullable = false, columnDefinition = "VARCHAR(14)")
	String cnpj = ""; // CNPJ - 00000000000000
    
    @NotNull(message = "O campo razão social da empresa não pode ser vazio.")
    @NotBlank(message = "O campo de razão social da empresa não pode ser vazio.")
    @Size(min=1, max=255, message = "A razão social deve ter no mínimo 1 e no máximo 255 caracteres.")
    @Column(name="corporate_reason", nullable = false, columnDefinition = "VARCHAR(255)")
    private String corporateReason = "";

    @NotNull(message = "O campo nome fantasia da empresa não pode ser vazio.")
    @NotBlank(message = "O campo nome fantasia da empresa não pode ser vazio.")
    @Size(min=1, max=255, message = "O nome fantasia precisa ter no mínimo 1 e no máximo 255 caracteres.")
    @Column(name="company", nullable = false, columnDefinition = "VARCHAR(255)")
    String company = "";

    @NotNull(message = "O campo canais de comunicação não pode ser vazio.")
    @NotEmpty(message = "O campo canais de comunicação não pode ser vazio.")
    @Column(name="communication_channel", nullable = false, columnDefinition = "_VARCHAR")   
    String[] communicationChannel = {};

    @Column(name="status", nullable=false, columnDefinition = "VARCHAR(40) DEFAULT 'enabled'")
	Status status = Status.ENABLED;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Date createdAt = new Date();

    @Column(name="updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    Date updatedAt = new Date();

    @Transient
	@JsonProperty(value = "partner_uuid", access = JsonProperty.Access.WRITE_ONLY)
	String partnerUuid = "";

	List<String> partners = new ArrayList<>();

    public String newUuid() {
		this.uuid = UUID.randomUUID().toString();
		return this.uuid;
	}

    @Override
    public String toString() {
        return "Enterprise[id=%d, company=%s, timezone=%s, address=%s, cnpj=%s, corporateReason=%s]".formatted(this.id, this.company, this.timezone, this.address, this.cnpj, this.corporateReason);
    }
}