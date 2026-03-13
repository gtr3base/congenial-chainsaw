package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.GarageCarDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoResponse;
import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.GarageCar;
import com.gtr3base.AvByAnalog.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = {NoteMapper.class})
public interface GarageMapper {

    @Mapping(source = "locked", target = "locked")
    @Mapping(source = "cars", target = "cars", qualifiedByName = "mapToGarageCarDTOList")
    @Mapping(source = "cars", target = "notes", qualifiedByName = "mapNotes")
    GarageInfoResponse toGarageInfoResponse(Garage garage);

    @Named("mapNotes")
    default List<Note> mapNotes(List<GarageCar> cars){
        return cars != null && !cars.isEmpty() ? cars.stream().flatMap(c -> c.getNotes().stream()).collect(Collectors.toList()) : null;
    }

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
                .userId(car.getGarage().getUser() != null ? Long.valueOf(car.getGarage().getUser().getId()) : null)
                .build();
    }
}
