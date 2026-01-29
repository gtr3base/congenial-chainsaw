package com.gtr3base.AvByAnalog.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AverageCarPriceSearchFilter implements CarFilter{
    private String carModel;
    private String carMake;
    private String generation;
    private String year;
}
