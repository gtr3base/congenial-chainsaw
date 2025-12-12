package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.enums.CarStatus;
import jakarta.validation.constraints.NotNull;

public record CarStatusUpdateDto(
        @NotNull(message = "New status is required")
        CarStatus status
) {
}
