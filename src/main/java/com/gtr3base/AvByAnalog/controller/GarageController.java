package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.GarageDTO;
import com.gtr3base.AvByAnalog.dto.GarageResponse;
import com.gtr3base.AvByAnalog.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/garage")
public class GarageController {
    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<GarageResponse> addGarage(@RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.addGarage(garageDTO));
    }

    @GetMapping
    public ResponseEntity<GarageResponse> getGarageByUserId() {
        return ResponseEntity.ok(garageService.getGarageByUserId());
    }

    @PutMapping
    public ResponseEntity<GarageResponse> updateGarage(@RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.updateGarage(garageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-car")
    public ResponseEntity<Void> addCarToGarage(@RequestBody CarCreateRequest carRequest){
        garageService.addCarToGarage(carRequest);
        return ResponseEntity.ok().build();
    }
}
