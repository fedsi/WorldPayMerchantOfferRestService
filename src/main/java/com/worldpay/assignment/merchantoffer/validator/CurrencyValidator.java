package com.worldpay.assignment.merchantoffer.validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.worldpay.assignment.merchantoffer.entity.CurrencyList;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {

    List<String> currency = Stream.of(CurrencyList.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return currency.contains(value);
    }
}