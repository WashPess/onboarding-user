package com.onboarding.user.onboardinguser.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Currency {
    
    @JsonEnumDefaultValue BRL("brl"),
    EUR("eur"),
    VOID("");


    private String code;

    private Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}