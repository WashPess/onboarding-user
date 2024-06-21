package com.onboarding.user.onboardinguser.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NationalityConverter implements AttributeConverter<Nationality, String> {
	
	@Override
    public String convertToDatabaseColumn(Nationality nt) {
        if (nt == null) {
            return null;
        }
        return nt.getCode();
    }

    @Override
    public Nationality convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Nationality.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}