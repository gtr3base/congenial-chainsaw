package com.gtr3base.AvByAnalog.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AveragePriceResponse(
        BigDecimal averagePrice,
        boolean success
        ) {
}
