package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.dto.AveragePriceResponse;
import com.gtr3base.AvByAnalog.dto.CarSpecification;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AverageCarPriceService {
    private final CarRepository carRepository;

    private static final int MIN_CARS_FOR_STATISTICS = 5;

    public AveragePriceResponse getAverageCarPrice(AverageCarPriceSearchFilter filter) {
        Specification<Car> spec = CarSpecification.getSpecs(filter);

        List<Car> cars =  carRepository.findAll(spec);

        if(cars.size() < MIN_CARS_FOR_STATISTICS){
            return AveragePriceResponse.builder().averagePrice(null).success(false).build();
        }

        BigDecimal sum = cars.stream()
                .map(Car::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avg = sum.divide(
                BigDecimal.valueOf(cars.size()),
                2,
                RoundingMode.HALF_UP
        );

        return AveragePriceResponse.builder().averagePrice(avg).success(true).build();
    }
}
