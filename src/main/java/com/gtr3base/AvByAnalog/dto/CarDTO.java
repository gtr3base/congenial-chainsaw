package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.annotations.ValidYearByModelId;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@ValidYearByModelId
public record CarDTO(
        @NotNull(message = "Make ID is required") Long makeId,

        @NotNull(message = "Model ID is required") Long modelId,

        @NotNull(message = "Generation ID is required") Long generationId,

        @NotNull
        @Min(value = 1886)
        Integer year,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal price,

        @NotBlank
        String description,

        @NotNull
        @Size(min = 17, max = 17)
        @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN code format")
        String vinCode
) {}