package com.onboarding.user.onboardinguser.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MariatalConverter implements AttributeConverter<Marital, String> {
	
	@Override
    public String convertToDatabaseColumn(Marital mt) {
        if (mt == null) {
            return null;
        }
        return mt.getCode();
    }

    @Override
    public Marital convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Marital.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}