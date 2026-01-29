package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CarSpecification {
    public static Specification<Car> getSpecs(CarSearchFilter filter){
        return (root, query, criterialBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getStatus() != null){
                predicates.add(criterialBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if(filter.getCarMake() != null){
                predicates.add(criterialBuilder.like(criterialBuilder.lower(root.get("carModel")),
                        "%" + filter.getCarMake().toLowerCase() + "%"));
            }

            if(filter.getMinPrice() != null){
                predicates.add(criterialBuilder.lessThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            return criterialBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Car> getSpecs(AverageCarPriceSearchFilter filter){
        return ((root, query, criteriaBuilder) ->  {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getCarMake() != null){
                predicates.add(criteriaBuilder.equal(root.get("carMake").get("name"), filter.getCarMake()));
            }

            if(filter.getCarModel() != null){
                predicates.add(criteriaBuilder.equal(root.get("carModel").get("name"), filter.getCarModel()));
            }

            if(filter.getYear() != null){
                predicates.add(criteriaBuilder.equal(root.get("year"), filter.getYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
