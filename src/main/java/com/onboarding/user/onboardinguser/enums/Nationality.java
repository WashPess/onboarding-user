package com.onboarding.user.onboardinguser.enums;

public enum Nationality {
    
    BRAZILIAN("brazilian"), AMERICAN("american"), SPANISH("spanish"), FRENCH("french"), VOID("");

    private String code;

    private Nationality(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
