package com.onboarding.user.onboardinguser.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Language {
    
    @JsonEnumDefaultValue PORTUGUESE("portuguese"),
    ENGLISH("english"),
    SPANISH("spanish"),
    FRENCH("french"),
    VOID("");

    private String code;

    private Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
