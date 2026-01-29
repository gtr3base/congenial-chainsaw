package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarStatusUpdateDto;
import com.gtr3base.AvByAnalog.dto.filter.CarSearchFilter;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import com.gtr3base.AvByAnalog.service.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDTO> addCar(@RequestBody @Valid CarCreateRequest carCreateRequest) {
        CarDTO car = carService.createCar(carCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CarDTO>> getCarsByStatus(@ModelAttribute CarSearchFilter filter, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(carService.searchCars(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarCreateRequest> getCarById(@PathVariable @NotNull Long id) {
        CarCreateRequest car = carService.getCarById(id);

        return ResponseEntity.ok(car);
    }

    @GetMapping("/admin/transition/{id}")
    public ResponseEntity<CarStatus[]> getCarTransitionById(@PathVariable @NotNull Long id) {
        CarStatus[] status = carService.getAvailableTransitions(id);

        return ResponseEntity.ok(status);
    }

    @PutMapping("/admin/status/{id}")
    public ResponseEntity<CarDTO> changeStatusById(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid CarStatusUpdateDto carStatusUpdateDto
    ){
        CarDTO car = carService.updateCarStatus(id, carStatusUpdateDto.status());

        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CarDTO> deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody @Valid CarCreateRequest carCreateRequest) {
        return ResponseEntity.ok(carService.updateCar(id, carCreateRequest));
    }
}