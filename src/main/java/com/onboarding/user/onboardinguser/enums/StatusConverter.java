package com.onboarding.user.onboardinguser.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {
	
	@Override
    public String convertToDatabaseColumn(Status st) {
        if (st == null) {
            return null;
        }
        return st.getCode();
    }

    @Override
    public Status convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Status.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}