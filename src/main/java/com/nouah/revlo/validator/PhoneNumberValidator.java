package com.nouah.revlo.validator;

import com.nouah.revlo.validator.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String PHONE_PATTERN = "^\\+234[0-9]{10}$";
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value.matches(PHONE_PATTERN);
    }



}
