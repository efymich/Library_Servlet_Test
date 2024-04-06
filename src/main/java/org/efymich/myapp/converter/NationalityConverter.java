package org.efymich.myapp.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.efymich.myapp.enums.Nationalities;

@Converter
public class NationalityConverter implements AttributeConverter<Nationalities,String> {
    @Override
    public String convertToDatabaseColumn(Nationalities nationalities) {
        return nationalities.toString();
    }

    @Override
    public Nationalities convertToEntityAttribute(String s) {
        return Nationalities.valueOf(s.toUpperCase());
    }
}
