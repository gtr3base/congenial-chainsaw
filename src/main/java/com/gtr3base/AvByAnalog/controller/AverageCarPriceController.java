package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.service.AverageCarPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/average-car-price")
public class AverageCarPriceController {
    private final AverageCarPriceService averageCarPriceService;

    public AverageCarPriceController(AverageCarPriceService averageCarPriceService) {
        this.averageCarPriceService = averageCarPriceService;
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getAverageCarPrice(@ModelAttribute AverageCarPriceSearchFilter filter) {
        return ResponseEntity.ok(averageCarPriceService.getAverageCarPrice(filter));
    }
}
