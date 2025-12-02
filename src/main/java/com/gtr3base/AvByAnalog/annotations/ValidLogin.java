package com.gtr3base.AvByAnalog.annotations;

import com.gtr3base.AvByAnalog.validators.LoginValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface ValidLogin {
    String message() default "Invalid login format or length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minUsernameLength() default 2;
    int maxUsernameLength() default 50;
    int minEmailLength() default 11;
    int maxEmailLength() default 100;
}
