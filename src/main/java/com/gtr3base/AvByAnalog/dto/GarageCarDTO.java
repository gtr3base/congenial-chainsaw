package com.gtr3base.AvByAnalog.dto;

import lombok.Builder;

@Builder
public record GarageCarDTO(
        Long garageId,
        Long userId
) {
}
