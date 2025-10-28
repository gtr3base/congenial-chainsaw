package com.gtr3base.AvByAnalog.annotations;

import com.gtr3base.AvByAnalog.validators.ValidGenerationYearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidGenerationYearValidator.class)
@Documented
public @interface ValidGenerationYear {
    String message() default "Year is not valid for the selected generation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String yearField() default "year";

    String generationField() default "generation";
}
