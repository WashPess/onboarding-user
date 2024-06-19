package com.onboarding.user.onboardinguser.enums;

public enum Gender {
    
    MALE("male"), FEMALE("female"), VOID("");

    private String code;

    private Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
