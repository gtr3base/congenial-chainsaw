package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.AveragePriceResponse;
import com.gtr3base.AvByAnalog.dto.filter.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.service.AverageCarPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/average-car-price")
public class AverageCarPriceController {
    private final AverageCarPriceService averageCarPriceService;

    public AverageCarPriceController(AverageCarPriceService averageCarPriceService) {
        this.averageCarPriceService = averageCarPriceService;
    }

    @GetMapping
    public ResponseEntity<AveragePriceResponse> getAverageCarPrice(@ModelAttribute AverageCarPriceSearchFilter filter) {
        return ResponseEntity.ok(averageCarPriceService.getAverageCarPrice(filter));
    }
}
