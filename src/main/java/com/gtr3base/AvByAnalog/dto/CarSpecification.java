package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class CarSpecification {
    public static Specification<Car> getSpecs(Integer userId, boolean isAdmin, CarSearchFilter filter){
        return (root, query, criterialBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(!isAdmin){
                predicates.add(criterialBuilder.equal(root.get("user").get("id"), userId));
            }

            if(filter.getStatus() != null){
                predicates.add(criterialBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if(filter.getCarMake() != null){
                predicates.add(criterialBuilder.like(criterialBuilder.lower(root.get("brand")),
                        "%" + filter.getCarMake().toLowerCase() + "%"));
            }

            if(filter.getMinPrice() != null){
                predicates.add(criterialBuilder.lessThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            return criterialBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
