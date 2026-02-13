package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.GarageResponse;
import com.gtr3base.AvByAnalog.entity.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GarageMapper {
    @Mapping(source = "cars", target = "cars")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "id", target = "garageId")
    GarageResponse toGarageResponse(Garage garage);
}
