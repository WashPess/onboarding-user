package com.onboarding.user.onboardinguser.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Nationality {
    
    @JsonEnumDefaultValue BRAZILIAN("brazilian"),
    AMERICAN("american"),
    SPANISH("spanish"),
    FRENCH("french"),
    VOID("");

    private String code;

    private Nationality(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
