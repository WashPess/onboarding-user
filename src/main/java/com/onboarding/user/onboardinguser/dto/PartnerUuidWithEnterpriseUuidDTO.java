package com.onboarding.user.onboardinguser.dto;


// PartnerUuidWithEnterpriseUuidDTO
public class PartnerUuidWithEnterpriseUuidDTO {

    // Atributos
    String partnerUuid;
    String enterpriseUuid; 
    
    // Construtor da classe
    public boolean isValid() {
        return this.partnerUuid != null && this.enterpriseUuid != null;
    }

    public String getPartnerUuid() {
        return this.partnerUuid;
    }

    public String getEnterpriseUuid() {
        return this.enterpriseUuid;
    }
}
