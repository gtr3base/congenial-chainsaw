package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.NoteResponse;
import com.gtr3base.AvByAnalog.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface NoteMapper {
    @Mapping(source = "garage.id", target = "garageId")
    @Mapping(source = "garageCar.car.id", target = "carId")
    @Mapping(source = "noteContent", target = "content")
    NoteResponse mapToNoteResponse(Note note);
}
