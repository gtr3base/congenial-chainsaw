package com.gtr3base.AvByAnalog.dto.filter.factory;

import com.gtr3base.AvByAnalog.dto.filter.CarFilter;
import com.gtr3base.AvByAnalog.dto.strategy.AverageCarPriceStrategy;
import com.gtr3base.AvByAnalog.dto.strategy.CarSpecificationStrategy;
import com.gtr3base.AvByAnalog.dto.strategy.StandardSearchStrategy;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.exceptions.SpecificationStrategyException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.SPECIFICATION_STRATEGY_EXCEPTION;

@Component
public class CarFilterFactory {
    private final List<CarSpecificationStrategy> strategies;

    public CarFilterFactory() {
        this.strategies = List.of(
                new StandardSearchStrategy(),
                new AverageCarPriceStrategy()
        );
    }

    public Specification<Car> getSpecs(CarFilter filter){
        return strategies.stream()
                .filter(strategy -> strategy.supports(filter))
                .findFirst()
                .map(strategy -> strategy.getSpecification(filter))
                .orElseThrow(() -> new SpecificationStrategyException(
                        String.format(SPECIFICATION_STRATEGY_EXCEPTION,filter.getClass().getSimpleName())));
    }
}
