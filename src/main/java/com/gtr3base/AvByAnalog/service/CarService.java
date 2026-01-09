package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarSearchFilter;
import com.gtr3base.AvByAnalog.dto.CarSpecification;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.CarGeneration;
import com.gtr3base.AvByAnalog.entity.CarModel;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.CarAction;
import com.gtr3base.AvByAnalog.enums.CarStatus;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public CarDTO createCar(@Valid CarCreateRequest carRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Car carToSave = carFromRequestMapper.toCar(carRequest);

        enrichCar(carRequest, carToSave, authentication);

        Car savedCar = carRepository.save(carToSave);

        return carFromRequestMapper.toResponse(savedCar);
    }

    public void deleteCarById(Long id) {
        Car car = carRepository.findCarById(id)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, id)));

        car.setPendingAction(CarAction.DELETE);

        carRepository.save(car);
    }

    public CarDTO updateCar(Long carId, @Valid CarCreateRequest carRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
    public CarDTO updateCarStatus(Long carId, CarStatus newStatus) {
        Car car = carFromRequestMapper.toCar(findCarById(carId));

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

    public Page<CarDTO> searchCars(CarSearchFilter filter,
                                   Pageable pageable) {

        Specification<Car> spec = CarSpecification.getSpecs(filter);

        Page<Car> cars = carRepository.findAll(spec, pageable);

        return cars.map(carFromRequestMapper::toResponse);
    }


    public CarCreateRequest getCarById(@NotNull Long id) {
        return findCarById(id);
    }

    public CarStatus[] getAvailableTransitions(@NotNull Long id) {
        Car car = carFromRequestMapper.toCar(findCarById(id));

        return car.getStatus().getAvailableTransitions();
    }

    public CarCreateRequest findCarById(Long id){
        Car car = carRepository.findCarById(id).orElseThrow(
                () -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, id))
        );
        return carFromRequestMapper.toCarCreateRequest(car);
    }

    private List<Car> findCarsByUser(Integer userId){
        return carRepository.findCarsByUserId(userId)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, userId)));
    }

    private void enrichCar(CarCreateRequest carRequest, Car carToSave, Authentication authentication) {
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
}