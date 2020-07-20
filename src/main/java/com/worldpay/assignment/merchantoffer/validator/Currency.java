package com.worldpay.assignment.merchantoffer.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface Currency {

    String message() default "Currency must be an ISO 4217 alphabetic code.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}