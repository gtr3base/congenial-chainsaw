package com.gtr3base.AvByAnalog.validators;

import com.gtr3base.AvByAnalog.annotations.ValidLogin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    private int minUsernameLength;
    private int maxUsernameLength;
    private int minEmailLength;
    private int maxEmailLength;

    @Override
    public void initialize(ValidLogin validLogin) {
        this.minUsernameLength = validLogin.minUsernameLength();
        this.maxUsernameLength = validLogin.maxUsernameLength();
        this.minEmailLength = validLogin.minEmailLength();
        this.maxEmailLength = validLogin.maxEmailLength();
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if(login == null){
            return true;
        }

        if(login.contains("@")){
            return login.length() >= minEmailLength && login.length() <= maxEmailLength;
        }

        else{
            return login.length() >= minUsernameLength && login.length() <= maxUsernameLength;
        }
    }
}
