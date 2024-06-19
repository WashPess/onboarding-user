package com.onboarding.user.onboardinguser.enums;

public enum Language {
    PORTUGUESE("portuguese"), ENGLISH("english"), SPANISH("spanish"), FRENCH("french"), VOID("");

    private String code;

    private Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
