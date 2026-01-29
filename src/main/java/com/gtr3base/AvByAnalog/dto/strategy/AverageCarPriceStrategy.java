package com.gtr3base.AvByAnalog.dto.strategy;

import com.gtr3base.AvByAnalog.dto.filter.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.dto.filter.CarFilter;
import com.gtr3base.AvByAnalog.entity.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AverageCarPriceStrategy implements CarSpecificationStrategy{
    @Override
    public boolean supports(CarFilter filter) {
        return filter instanceof AverageCarPriceSearchFilter;
    }

    @Override
    public Specification<Car> getSpecification(CarFilter filter) {
        AverageCarPriceSearchFilter searchFilter = (AverageCarPriceSearchFilter) filter;

        return ((root, query, criteriaBuilder) ->  {
            List<Predicate> predicates = new ArrayList<>();

            if(searchFilter.getCarMake() != null){
                predicates.add(criteriaBuilder.equal(root.get("carMake").get("name"), searchFilter.getCarMake()));
            }

            if(searchFilter.getGeneration() != null){
                predicates.add(criteriaBuilder.equal(root.get("generation").get("name"), searchFilter.getGeneration()));
            }

            if(searchFilter.getCarModel() != null){
                predicates.add(criteriaBuilder.equal(root.get("carModel").get("name"), searchFilter.getCarModel()));
            }

            if(searchFilter.getYear() != null){
                predicates.add(criteriaBuilder.equal(root.get("year"), searchFilter.getYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
