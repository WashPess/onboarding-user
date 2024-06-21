package com.onboarding.user.onboardinguser.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Gender {
    
    @JsonEnumDefaultValue MALE("male"),
    FEMALE("female"),
    VOID("");

    private String code;

    private Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
