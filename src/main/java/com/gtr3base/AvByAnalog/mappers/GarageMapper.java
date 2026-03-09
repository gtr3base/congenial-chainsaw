package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.GarageCarDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoResponse;
import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.GarageCar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {NoteMapper.class})
public interface GarageMapper {

    @Mapping(source = "locked", target = "locked")
    @Mapping(source = "cars", target = "cars", qualifiedByName = "mapToGarageCarDTOList")
    @Mapping(source = "notes", target = "notes")
    GarageInfoResponse toGarageInfoResponse(Garage garage);

    @Named("mapToGarageCarDTOList")
    default List<GarageCarDTO> mapToGarageCarDTOList(List<GarageCar> cars){
        if(cars == null || cars.isEmpty()){
            return null;
        }

        return cars.stream()
                .map(this::mapToGarageCarDTO)
                .toList();
    }

    default GarageCarDTO mapToGarageCarDTO(GarageCar car){
        if(car == null){
            return null;
        }

        return GarageCarDTO.builder()
                .garageId(car.getGarage() != null ? car.getGarage().getId() : null)
                .userId(car.getOwner() != null ? Long.valueOf(car.getOwner().getId()) : null)
                .build();
    }
}
