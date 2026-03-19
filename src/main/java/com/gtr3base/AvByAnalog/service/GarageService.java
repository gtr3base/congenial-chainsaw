package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.GarageCarDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoResponse;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.GarageCar;
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

import java.util.ArrayList;
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

        garage.getCars().add(carFromRequestMapper.toGarageCar(car));

        garageRepository.save(garage);
    }

    public GarageInfoResponse addGarage(GarageInfoDTO garageInfoDTO){
        User user = getCurrentUser();

        Garage garage = Garage.builder()
                .locked(garageInfoDTO.locked())
                .user(user)
                .build();

        garage.setUser(user);

        garageRepository.save(garage);

        GarageCarDTO garageCarDTO = GarageCarDTO
                .builder()
                .userId(Long.valueOf(user.getId()))
                .garageId(garage.getId())
                .build();

        List<GarageCarDTO> garageCarDTOS = new ArrayList<>();
        garageCarDTOS.add(garageCarDTO);

        return GarageInfoResponse.builder()
                .locked(garage.getLocked())
                .cars(garageCarDTOS)
                .build();
    }

    public GarageInfoResponse updateGarage(GarageInfoDTO garageInfoDTO){
        Garage garage = garageRepository.findById(garageInfoDTO.garageId())
                .orElseThrow(() -> new GarageNotFoundException(String.format(GARAGE_NOT_FOUND, garageInfoDTO.garageId())));

        User user = getCurrentUser();

        List<Note> notes = noteRepository.findAllByGarageCarOwnerId(user.getId());

        Car carToAdd = carRepository.findCarById(garageInfoDTO.carId())
                        .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID,  garageInfoDTO.carId())));

        GarageCar gCar = carFromRequestMapper.toGarageCar(carToAdd);
        gCar.setNotes(notes);

        if(garage.getCars() != null && !garage.getCars().isEmpty()){
            garage.getCars().add(gCar);
        }

        garage.setLocked(garageInfoDTO.locked());

        garageRepository.save(garage);

        return garageMapper.toGarageInfoResponse(garage);
    }

    public void deleteGarage(Long id) {
        garageRepository.deleteById(id);
    }

    public GarageInfoResponse getGarageByUserId(){
        User user = getCurrentUser();
        Garage garage = garageRepository.findByUser(user)
                .orElseThrow(() -> new GarageNotFoundException(String.format(GARAGE_NOT_FOUND,  user.getUsername())));
        return garageMapper.toGarageInfoResponse(garage);
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, authentication.getName())));
    }
}
