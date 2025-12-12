package com.gtr3base.AvByAnalog.validators;

import com.gtr3base.AvByAnalog.annotations.ValidYearByModelId;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.entity.CarGeneration;
import com.gtr3base.AvByAnalog.entity.CarModel;
import com.gtr3base.AvByAnalog.repository.CarModelRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class YearByModelIdValidator implements ConstraintValidator<ValidYearByModelId, CarDTO> {

    private final CarModelRepository carModelRepository;

    public YearByModelIdValidator(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    @Override
    public void initialize(ValidYearByModelId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(CarDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.modelId() == null || dto.year() == null) {
            return true;
        }

        CarModel  carModel = carModelRepository.findById(dto.modelId()).orElse(null);

        if (carModel == null) {
            return true;
        }

        boolean isValid = isYearValidForModel(dto.year(), carModel);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("year").addConstraintViolation();

        }

        return isValid;
    }

    private boolean isYearValidForModel(int year, CarModel carModel) {
        List<CarGeneration> generations = carModel.getGenerations();

        if(generations == null || generations.isEmpty()) {
            return true;
        }

        int currentYear = LocalDate.now().getYear();

        return generations.stream()
                .anyMatch(gen -> {
                    int start = gen.getYearStart();

                    int end = (gen.getYearEnd() != null) ? gen.getYearEnd() : currentYear;

                    return year >= start && year <= end;
                });
    }
}
