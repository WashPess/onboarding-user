package com.onboarding.user.onboardinguser.dto;


// PartnerUuidWithEnterpriseUuidDTO
public class PartnerUuidWithEnterpriseUuidDTO {

    String partnerUuid;
    String enterpriseUuid; 
    
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
