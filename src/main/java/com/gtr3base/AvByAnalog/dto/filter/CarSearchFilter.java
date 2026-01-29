package com.gtr3base.AvByAnalog.dto.filter;

import com.gtr3base.AvByAnalog.enums.CarStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarSearchFilter implements CarFilter{
    private CarStatus status;
    private String carMake;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
