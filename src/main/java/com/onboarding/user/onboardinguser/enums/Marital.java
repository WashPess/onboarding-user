package com.onboarding.user.onboardinguser.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Marital {
    
    @JsonEnumDefaultValue SINGLE("single"), 
    MARRIED("married"),
    WIDOWER("widower"), 
    SEPARATE("separate"), 
    DIVORCED("divorced"), 
    VOID("");

    private String code;

    private Marital(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
