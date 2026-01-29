package com.gtr3base.AvByAnalog.exceptions;

public class ErrorHandler {
    public static final String USER_NOT_FOUND = "User not found with ID: %s";
    public static final String MODEL_NOT_FOUND = "Model not found with ID: %s";
    public static final String CAR_GENERATION_NOT_FOUND = "Generation not found with ID: %s";
    public static final String CAR_NOT_FOUND_BY_ID = "Car(s) with ID: %s not found";
    public static final String INVALID_CAR_TRANSITION = "Invalid transition: Cannot change status from %s to %s";
    public static final String CAR_NOT_FOUND_BY_VIN = "Invalid vin: %s not found";
    public static final String ACCESS_DENIED_FOR_USER_ROLE = "Access denied for user with role: %s";
    public static final String INVALID_YEAR_GENERATION = "The year is not valid for the selected car generation";
    public static final String SPECIFICATION_STRATEGY_EXCEPTION = "No specification strategy found for filter: %s";
}
