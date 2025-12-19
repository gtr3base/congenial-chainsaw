package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.dto.CarSearchFilter;
import com.gtr3base.AvByAnalog.dto.CarSpecification;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.CarGeneration;
import com.gtr3base.AvByAnalog.entity.CarModel;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.CarAction;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import com.gtr3base.AvByAnalog.enums.UserRole;
import com.gtr3base.AvByAnalog.exceptions.CarGenerationNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.CarNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.CarTransitionException;
import com.gtr3base.AvByAnalog.exceptions.ModelNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.RoleAccessDeniedException;
import com.gtr3base.AvByAnalog.exceptions.ValidYearForGenerationException;
import com.gtr3base.AvByAnalog.mappers.CarFromRequestMapper;
import com.gtr3base.AvByAnalog.repository.CarGenerationRepository;
import com.gtr3base.AvByAnalog.repository.CarModelRepository;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.ACCESS_DENIED_FOR_USER_ROLE;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.CAR_GENERATION_NOT_FOUND;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.CAR_NOT_FOUND_BY_ID;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.INVALID_CAR_TRANSITION;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.INVALID_YEAR_GENERATION;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.MODEL_NOT_FOUND;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.USER_NOT_FOUND;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarFromRequestMapper carFromRequestMapper;

    private final UserRepository userRepository;
    private final CarModelRepository carModelRepository;
    private final CarGenerationRepository carGenerationRepository;

    public CarService(CarRepository carRepository, CarFromRequestMapper carFromRequestMapper, UserRepository userRepository, CarModelRepository carModelRepository, CarGenerationRepository carGenerationRepository) {
        this.carRepository = carRepository;
        this.carFromRequestMapper = carFromRequestMapper;
        this.userRepository = userRepository;
        this.carModelRepository = carModelRepository;
        this.carGenerationRepository = carGenerationRepository;
    }

    private Car findCarById(Long id){
        return carRepository.findCarById(id).orElseThrow(
                () -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, id))
        );
    }

    private Car findCarByUser(Long userId){
        return carRepository.findCarByUserId(Math.toIntExact(userId))
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, userId)));
    }

    private void enrichCar(CarDTO carRequest, Car carToSave, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, authentication.getName())));
        carToSave.setUser(user);

        CarModel model = carModelRepository.findById(carRequest.modelId())
                .orElseThrow(() -> new ModelNotFoundException(String.format(MODEL_NOT_FOUND, carRequest.modelId())));
        carToSave.setCarModel(model);

        CarGeneration generation = carGenerationRepository.findById(carRequest.generationId())
                .orElseThrow(() -> new CarGenerationNotFoundException(String.format(CAR_GENERATION_NOT_FOUND, carRequest.generationId())));
        carToSave.setGeneration(generation);

        carToSave.setPendingAction(CarAction.CREATE);
    }

    @Transactional
    public CarResponse createCar(@Valid CarDTO carRequest, Authentication authentication) {
        Car carToSave = carFromRequestMapper.toCar(carRequest);

        enrichCar(carRequest, carToSave, authentication);

        Car savedCar = carRepository.save(carToSave);

        return carFromRequestMapper.toResponse(savedCar);
    }

    public CarResponse deleteCar(Long id, Authentication authentication) {
        Car car = carRepository.findCarById(id)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, id)));

        car.setPendingAction(CarAction.DELETE);

        carRepository.save(car);
        return carFromRequestMapper.toResponse(car);
    }

    @Transactional
    public CarResponse updateCar(Long carId, @Valid CarDTO carRequest, Authentication authentication) {
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, login)));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, carRequest.vinCode())));

        if(!user.getRole().isAdmin() && !car.getUser().getId().equals(user.getId())) {
            throw new RoleAccessDeniedException(String.format(ACCESS_DENIED_FOR_USER_ROLE, user.getRole()));
        }

        car.setPrice(carRequest.price());
        car.setDescription(carRequest.description());
        car.setYear(carRequest.year());
        car.setVinCode(carRequest.vinCode());

        if (car.getCarModel().getId() != Math.toIntExact(carRequest.modelId())) {
            CarModel model = carModelRepository.findById(carRequest.modelId())
                    .orElseThrow(() -> new ModelNotFoundException(String.format(MODEL_NOT_FOUND, carRequest.modelId())));
            car.setCarModel(model);
        }

        if (car.getGeneration().getId() != Math.toIntExact(carRequest.generationId())) {
            CarGeneration generation = carGenerationRepository.findById(carRequest.generationId())
                    .orElseThrow(() -> new CarGenerationNotFoundException(String.format(CAR_GENERATION_NOT_FOUND, carRequest.generationId())));
            car.setGeneration(generation);
        }

        if (!car.isValidYearForGeneration()) {
            throw new ValidYearForGenerationException(INVALID_YEAR_GENERATION);
        }

        car.setStatus(CarStatus.PENDING);
        car.setPendingAction(CarAction.UPDATE);

        Car savedCar = carRepository.save(car);
        return carFromRequestMapper.toResponse(savedCar);
    }

    @Transactional
    public CarResponse updateCarStatus(Long carId, CarStatus newStatus) {
        Car car = findCarById(carId);

        if(!car.getStatus().canTransitionTo(newStatus)){
            throw new CarTransitionException(String.format(INVALID_CAR_TRANSITION,car.getStatus(),newStatus));
        }

        car.setStatus(newStatus);

        CarAction action = car.getPendingAction();

        if(car.isApproved()){
            if(action == CarAction.CREATE || action == CarAction.UPDATE){
                car.setPendingAction(CarAction.NONE);
                carRepository.save(car);
            } else if (action == CarAction.DELETE) {
                carRepository.delete(car);
            }
        }

        return carFromRequestMapper.toResponse(car);
    }

    public Page<CarResponse> searchCars(CarSearchFilter filter,
                                        Pageable pageable,
                                        Authentication authentication) {
        String login = authentication.getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, login)));

        boolean isAdmin = (user.getRole() == UserRole.ADMIN);

        Specification<Car> spec = CarSpecification.getSpecs(user.getId(), isAdmin, filter);

        Page<Car> cars = carRepository.findAll(spec, pageable);

        return cars.map(carFromRequestMapper::toResponse);
    }


    public CarResponse getCarById(@NotNull Long id) {
        Car car = findCarById(id);

        return carFromRequestMapper.toResponse(car);
    }

    public CarStatus[] getAvailableTransitions(@NotNull Long id) {
        Car car = findCarById(id);

        return car.getStatus().getAvailableTransitions();
    }
}