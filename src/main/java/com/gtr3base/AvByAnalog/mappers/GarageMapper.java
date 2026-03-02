package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.GarageInfoResponse;
import com.gtr3base.AvByAnalog.entity.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {CarFromRequestMapper.class, NoteMapper.class})
public interface GarageMapper {
    @Mapping(source = "car", target = "car")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "id", target = "garageId")
    GarageInfoResponse toGarageInfoResponse(Garage garage);
}
