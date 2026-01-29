package com.gtr3base.AvByAnalog.dto.strategy;

import com.gtr3base.AvByAnalog.dto.filter.CarFilter;
import com.gtr3base.AvByAnalog.dto.filter.CarSearchFilter;
import com.gtr3base.AvByAnalog.entity.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StandardSearchStrategy implements CarSpecificationStrategy{
    @Override
    public boolean supports(CarFilter filter) {
        return filter.getClass().equals(CarSearchFilter.class);
    }

    @Override
    public Specification<Car> getSpecification(CarFilter filter) {
        CarSearchFilter searchFilter = (CarSearchFilter) filter;

        return (root, query, criterialBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(searchFilter.getStatus() != null){
                predicates.add(criterialBuilder.equal(root.get("status"), searchFilter.getStatus()));
            }

            if(searchFilter.getCarMake() != null){
                predicates.add(criterialBuilder.like(criterialBuilder.lower(root.get("carModel")),
                        "%" + searchFilter.getCarMake().toLowerCase() + "%"));
            }

            if(searchFilter.getMinPrice() != null){
                predicates.add(criterialBuilder.lessThanOrEqualTo(root.get("price"), searchFilter.getMinPrice()));
            }

            return criterialBuilder.and(predicates.toArray(new Predicate[0]));
        };
}}
