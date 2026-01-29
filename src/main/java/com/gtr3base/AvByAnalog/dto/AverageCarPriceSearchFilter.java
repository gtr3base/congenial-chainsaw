package com.gtr3base.AvByAnalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AverageCarPriceSearchFilter{
    private String carModel;
    private String carMake;
    private String year;
}
