package com.onboarding.user.onboardinguser.enums;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {
	
	@Override
    public String convertToDatabaseColumn(Language lg) {
        if (lg == null) {
            return null;
        }
        return lg.getCode();
    }

    @Override
    public Language convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Language.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}