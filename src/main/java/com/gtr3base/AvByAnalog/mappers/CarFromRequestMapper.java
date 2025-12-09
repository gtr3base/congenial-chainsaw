package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.CarRequest;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, imports = HttpStatus.class)
public interface CarFromRequestMapper {

    @Mapping(target = "httpStatus", expression = "java(HttpStatus.CREATED)")
    CarResponse toResponse(Car car);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "carModel", ignore = true)
    @Mapping(target = "generation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "priceHistory", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    Car toCar(CarRequest carRequest);
}
