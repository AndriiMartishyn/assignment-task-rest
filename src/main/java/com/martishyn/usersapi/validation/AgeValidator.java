package com.martishyn.usersapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
    @Value("${validation.legit.age}")
    private int legitAge;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value!= null && (LocalDate.now().getYear() - value.getYear() >= legitAge);
    }
}
