package com.gtr3base.AvByAnalog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GarageInfoResponse(
        Boolean locked,
        Long garageId,
        CarDTO car,
        List<NoteResponse> notes
) {}
