package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.enums.CarStatus;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarResponse {
    private Long id;
    private Long userId;
    private String username;

    private String carMake;
    private String carModel;
    private String carGeneration;

    private String vinCode;

    private String description;

    private BigDecimal price;

    private Integer year;

    private CarStatus carStatus;
}