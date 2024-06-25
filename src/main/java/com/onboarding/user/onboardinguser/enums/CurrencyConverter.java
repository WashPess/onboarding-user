package com.onboarding.user.onboardinguser.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {
	
	@Override
    public String convertToDatabaseColumn(Currency gd) {
        if (gd == null) {
            return null;
        }
        return gd.getCode();
    }

    @Override
    public Currency convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Currency.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}