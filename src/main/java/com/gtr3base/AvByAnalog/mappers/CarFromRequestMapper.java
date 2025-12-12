package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, imports = HttpStatus.class)
public interface CarFromRequestMapper {

    @Mapping(source = "carModel.name", target = "carModel")
    @Mapping(source = "generation.name", target = "carGeneration")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "carModel.carMake.name", target = "carMake")
    @Mapping(source = "status", target = "carStatus")
    @Mapping(source = "pendingAction", target = "carAction")
    CarResponse toResponse(Car car);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "carModel", ignore = true)
    @Mapping(target = "generation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "priceHistory", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pendingAction", ignore = true)
    Car toCar(CarDTO carRequest);
}
