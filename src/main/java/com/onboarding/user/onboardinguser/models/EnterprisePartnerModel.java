package com.onboarding.user.onboardinguser.models;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "enterprise_partner_association")
public class EnterprisePartnerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", columnDefinition = "BIGSERIAL PRIMARY KEY")
	@JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
	private Long id;


    @Column(name = "enterprise_uuid", unique = true, columnDefinition = "VARCHAR(64)")
    public String enterpriseUuid;

    @Column(name = "partner_uuid", unique = true, columnDefinition = "VARCHAR(64)")
    public String partnerUuid;

}