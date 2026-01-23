package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.enums.CarAction;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CarDTO (
    Long id,
    Long userId,
    String username,

    String carMake,
    String carModel,
    String carGeneration,

    String vinCode,

    String description,

    BigDecimal price,

    Integer year,

    CarStatus carStatus,

    CarAction carAction
){}