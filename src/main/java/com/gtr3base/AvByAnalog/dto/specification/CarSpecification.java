package com.gtr3base.AvByAnalog.dto.specification;

import com.gtr3base.AvByAnalog.dto.filter.CarFilter;
import com.gtr3base.AvByAnalog.dto.filter.factory.CarFilterFactory;
import com.gtr3base.AvByAnalog.entity.Car;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {
    private static final CarFilterFactory filterFactory = new CarFilterFactory();

    public static Specification<Car> getSpecs(CarFilter filter){
        return filterFactory.getSpecs(filter);
    }
}
