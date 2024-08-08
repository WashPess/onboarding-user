package com.onboarding.user.onboardinguser.dto;


// PartnerUuidWithEnterpriseUuidDTO
public class PartnerUuidWithEnterpriseUuidDTO {
    String uuid;
    String enterpriseUuid; 
    
public boolean isValid() {
        return this.uuid != null && this.enterpriseUuid != null;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getEnterpriseUuid() {
        return this.enterpriseUuid;
    }

}
