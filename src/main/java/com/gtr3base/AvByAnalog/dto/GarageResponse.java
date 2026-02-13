package com.gtr3base.AvByAnalog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GarageResponse(
        Boolean locked,
        Long garageId,
        List<CarDTO> cars,
        List<NoteResponse> notes
) {}
