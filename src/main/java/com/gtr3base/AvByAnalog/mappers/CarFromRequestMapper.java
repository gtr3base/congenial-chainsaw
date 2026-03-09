package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.GarageCar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CarFromRequestMapper {

    @Mapping(source = "carModel.name", target = "carModel")
    @Mapping(source = "generation.name", target = "carGeneration")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "carModel.carMake.name", target = "carMake")
    @Mapping(source = "status", target = "carStatus")
    @Mapping(source = "pendingAction", target = "carAction")
    CarDTO toCarDTO(Car car);

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
    Car toCar(CarCreateRequest carRequest);

    @Mapping(source = "carModel.id", target = "modelId")
    @Mapping(source = "carModel.carMake.id", target = "makeId")
    @Mapping(source = "generation.id", target = "generationId")
    CarCreateRequest toCarCreateRequest(Car car);

    @Mapping(source = "user", target = "owner")
    @Mapping(target = "garage", ignore = true)
    GarageCar toGarageCar(Car car);
}
