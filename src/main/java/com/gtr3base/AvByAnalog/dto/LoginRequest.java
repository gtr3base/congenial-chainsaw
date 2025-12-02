package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.annotations.ValidLogin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @ValidLogin @NotBlank String login,
        @NotBlank @Size(min = 6, max = 255) @NotBlank String password
)
{ }
