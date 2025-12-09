package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.CarRequest;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.dto.CarStatusUpdateDto;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import com.gtr3base.AvByAnalog.service.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponse> addCar(@RequestBody @Valid CarRequest carRequest, Authentication authentication) {
        CarResponse car = carService.createCar(carRequest,authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @PostMapping("/status")
    public ResponseEntity<List<CarResponse>> getCarsByStatus(@RequestParam @NotNull CarStatus status, Authentication authentication) {
        return ResponseEntity.ok(carService.getCarsByStatus(status, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable @NotNull Long id) {
        CarResponse car = carService.getCarById(id);

        return ResponseEntity.ok(car);
    }

    @GetMapping("/admin/transition/{id}")
    public ResponseEntity<CarStatus[]> getCarTransitionById(@PathVariable @NotNull Long id) {
        CarStatus[] status = carService.getAvailableTransitions(id);

        return ResponseEntity.ok(status);
    }

    @PutMapping("/admin/status/{id}")
    public ResponseEntity<CarResponse> approveCarById(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid CarStatusUpdateDto carStatusUpdateDto
    ){
        CarResponse car = carService.updateCarStatus(id, carStatusUpdateDto.status());

        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/delete/{vin}")
    public ResponseEntity<CarResponse> deleteCar(@PathVariable String vin, Authentication authentication) {
        return ResponseEntity.ok(carService.deleteCar(vin, authentication));
    }

    @PutMapping
    public ResponseEntity<CarResponse> updateCar(@RequestBody @Valid CarRequest carRequest, Authentication authentication) {
        return ResponseEntity.ok(carService.updateCar(carRequest, authentication));
    }
}