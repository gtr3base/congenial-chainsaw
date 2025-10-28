package com.gtr3base.AvByAnalog.validators;

import com.gtr3base.AvByAnalog.annotations.ValidGenerationYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class ValidGenerationYearValidator implements ConstraintValidator<ValidGenerationYear, LocalDate> {
    private String yearField;
    private String generationField;
    @Override
    public void initialize(ValidGenerationYear constraintAnnotation) {
        this.yearField = constraintAnnotation.yearField();
        this.generationField = constraintAnnotation.generationField();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            BeanWrapper beanWrapper = new BeanWrapperImpl(value);

            Integer year = (Integer) beanWrapper.getPropertyValue(yearField);
            Object generation = beanWrapper.getPropertyValue(generationField);

            if (year == null || generation == null) {
                return true;
            }

            BeanWrapper generationWrapper = new BeanWrapperImpl(generation);
            Integer yearStart = (Integer) generationWrapper.getPropertyValue("yearStart");
            Integer yearEnd = (Integer) generationWrapper.getPropertyValue("yearEnd");

            if (yearStart == null) {
                return false;
            }

            int currentYear = LocalDate.now().getYear();
            int effectiveYearEnd = yearEnd != null ?
                    yearEnd : currentYear;

            boolean isValid = year >= yearStart && year <= effectiveYearEnd;

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Year " + year + " is not valid for generation " +
                                getGenerationName(generation) + " (" + yearStart + "-" +
                                (yearEnd != null ? yearEnd : "present") + ")"
                ).addPropertyNode(yearField).addConstraintViolation();
            }
            return isValid;
        }catch (Exception e){
            return false;
            }
        }

    private String getGenerationName(Object generation) {
        try {
            BeanWrapper wrapper = new BeanWrapperImpl(generation);
            String name = (String) wrapper.getPropertyValue("name");
            return name != null ? name : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
