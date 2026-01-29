package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.dto.CarSpecification;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AverageCarPriceService {
    private final CarRepository carRepository;

    public BigDecimal getAverageCarPrice(AverageCarPriceSearchFilter filter) {
        Specification<Car> spec = CarSpecification.getSpecs(filter);

        List<Car> cars =  carRepository.findAll(spec);

        return cars.stream()
                .map(Car::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
    }
}
