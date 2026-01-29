package com.gtr3base.AvByAnalog.dto.strategy;

import com.gtr3base.AvByAnalog.dto.filter.CarFilter;
import com.gtr3base.AvByAnalog.entity.Car;
import org.springframework.data.jpa.domain.Specification;

public interface CarSpecificationStrategy {
    boolean supports(CarFilter filter);

    Specification<Car> getSpecification(CarFilter filter);
}
