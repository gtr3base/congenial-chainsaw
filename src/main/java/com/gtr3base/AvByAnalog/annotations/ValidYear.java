package com.gtr3base.AvByAnalog.annotations;

import com.gtr3base.AvByAnalog.validators.YearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValidator.class)
public @interface ValidYear {
    String message() default "Year cannot be too far in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
