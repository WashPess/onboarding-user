package com.onboarding.user.onboardinguser.enums;

public enum Status {
    
   ENABLED("enabled"), DISABLED("disabled"), VOID(""), LOCKED("locked");

    private String code;

    private Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}