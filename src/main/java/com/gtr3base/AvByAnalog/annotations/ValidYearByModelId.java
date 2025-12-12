package com.gtr3base.AvByAnalog.annotations;

import com.gtr3base.AvByAnalog.validators.YearByModelIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearByModelIdValidator.class)
public @interface ValidYearByModelId {
    String message() default "The year is not valid for the selected car model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
