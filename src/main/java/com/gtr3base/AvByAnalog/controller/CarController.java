package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.dto.CarSearchFilter;
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
    public ResponseEntity<CarResponse> addCar(@RequestBody @Valid CarDTO carDTO, Authentication authentication) {
        CarResponse car = carService.createCar(carDTO,authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> getCarsByStatus(@ModelAttribute CarSearchFilter filter, Authentication authentication) {
        return ResponseEntity.ok(carService.searchCars(filter, authentication));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CarResponse> deleteCar(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(carService.deleteCar(id, authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @RequestBody @Valid CarDTO carDTO, Authentication authentication) {
        return ResponseEntity.ok(carService.updateCar(id, carDTO, authentication));
    }
}