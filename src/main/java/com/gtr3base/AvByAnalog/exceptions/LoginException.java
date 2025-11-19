package com.gtr3base.AvByAnalog.exceptions;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super("Login: " + message + " is already in use");
    }
}
