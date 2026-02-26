package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.GarageDTO;
import com.gtr3base.AvByAnalog.dto.GarageResponse;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.Note;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.exceptions.CarNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.GarageNotFoundException;
import com.gtr3base.AvByAnalog.mappers.CarFromRequestMapper;
import com.gtr3base.AvByAnalog.mappers.GarageMapper;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import com.gtr3base.AvByAnalog.repository.GarageRepository;
import com.gtr3base.AvByAnalog.repository.NoteRepository;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.CAR_NOT_FOUND_BY_ID;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.GARAGE_NOT_FOUND;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.USER_NOT_FOUND;

@Service
public class GarageService {
    private final GarageRepository garageRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final GarageMapper garageMapper;
    private final CarFromRequestMapper carFromRequestMapper;

    public GarageService(GarageRepository garageRepository, NoteRepository noteRepository, UserRepository userRepository, CarRepository carRepository, GarageMapper garageMapper, CarFromRequestMapper carFromRequestMapper) {
        this.garageRepository = garageRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.garageMapper = garageMapper;
        this.carFromRequestMapper = carFromRequestMapper;
    }

    public void addCarToGarage(CarCreateRequest carRequest){
        User user = getCurrentUser();

        Garage garage = garageRepository.findByUser(user)
                .orElseThrow(() -> new GarageNotFoundException(
                        String.format(GARAGE_NOT_FOUND, user.getUsername())
                ));

        Car car = carFromRequestMapper.toCar(carRequest);

        car.setGarage(garage);

        garage.getCars().add(car);
        garageRepository.save(garage);
    }

    public GarageResponse addGarage(GarageDTO garageDTO){
        User user = getCurrentUser();

        Car car = carRepository.findById(garageDTO.carId())
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, garageDTO.carId())));

        List<Car> cars = List.of(car);

        Garage garage = Garage.builder()
                .locked(garageDTO.locked())
                .cars(cars)
                .user(user)
                .build();

        User owner = getCurrentUser();

        garage.setUser(owner);

        garageRepository.save(garage);

        return GarageResponse.builder()
                .garageId(garage.getId())
                .locked(garage.isLocked())
                .build();
    }

    public GarageResponse updateGarage(GarageDTO garageDTO){
        Garage garage = garageRepository.findById(garageDTO.garageId())
                .orElseThrow(() -> new GarageNotFoundException(String.format(GARAGE_NOT_FOUND, garageDTO.garageId())));

        User user = getCurrentUser();

        List<Note> notes = noteRepository.findAllByUserId(user.getId());

        Car carToAdd = carRepository.findCarById(garageDTO.carId())
                        .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID,  garageDTO.carId())));

        List<Car> cars = garage.getCars();

        cars.add(carToAdd);

        garage.setLocked(garageDTO.locked());
        garage.setNotes(notes);
        garage.setCars(cars);

        garageRepository.save(garage);

        return garageMapper.toGarageResponse(garage);
    }

    public void deleteGarage(Long id) {
        garageRepository.deleteById(id);
    }

    public GarageResponse getGarageByUserId(){
        User user = getCurrentUser();
        Garage garage = garageRepository.findByUser(user)
                .orElseThrow(() -> new GarageNotFoundException(String.format(GARAGE_NOT_FOUND,  user.getUsername())));
        return garageMapper.toGarageResponse(garage);
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, authentication.getName())));
    }
}
