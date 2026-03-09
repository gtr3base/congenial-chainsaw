package com.gtr3base.AvByAnalog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GarageInfoResponse(
        Boolean locked,
        List<GarageCarDTO> cars,
        List<NoteResponse> notes
) {}
