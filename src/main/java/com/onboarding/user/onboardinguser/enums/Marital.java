package com.onboarding.user.onboardinguser.enums;

public enum Marital {
    
    MARRIED("married"), SINGLE("single"), WIDOWER("widower"), SEPARATE("separate"), DIVORCED("divorced"), VOID("");

    private String code;

    private Marital(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
