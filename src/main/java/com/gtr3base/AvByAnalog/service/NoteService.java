package com.gtr3base.AvByAnalog.service;

import com.gtr3base.AvByAnalog.dto.NoteDTO;
import com.gtr3base.AvByAnalog.dto.NoteResponse;
import com.gtr3base.AvByAnalog.dto.NoteUpdateRequest;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.GarageCar;
import com.gtr3base.AvByAnalog.entity.Note;
import com.gtr3base.AvByAnalog.entity.NoteContent;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.exceptions.CarNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.CarNotInGarageException;
import com.gtr3base.AvByAnalog.exceptions.GarageNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.NoteNotFoundException;
import com.gtr3base.AvByAnalog.mappers.GarageMapper;
import com.gtr3base.AvByAnalog.mappers.NoteMapper;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import com.gtr3base.AvByAnalog.repository.GarageCarRepository;
import com.gtr3base.AvByAnalog.repository.GarageRepository;
import com.gtr3base.AvByAnalog.repository.NoteRepository;
import com.gtr3base.AvByAnalog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.CAR_NOT_FOUND_BY_ID;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.CAR_NOT_IN_GARAGE;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.GARAGE_NOT_FOUND;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.NOTE_NOT_FOUND;
import static com.gtr3base.AvByAnalog.exceptions.ErrorHandler.USER_NOT_FOUND;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final GarageRepository garageRepository;
    private final CarRepository carRepository;
    private final NoteMapper noteMapper;
    private final GarageCarRepository garageCarRepository;
    private final GarageMapper garageMapper;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository, GarageRepository garageRepository, CarRepository carRepository, NoteMapper noteMapper, GarageCarRepository garageCarRepository, GarageMapper garageMapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.garageRepository = garageRepository;
        this.carRepository = carRepository;
        this.noteMapper = noteMapper;
        this.garageCarRepository = garageCarRepository;
        this.garageMapper = garageMapper;
    }

    public NoteResponse createNote(NoteDTO noteDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findByLogin(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND, authentication.getName())));

        Garage garage = garageRepository.findByUser(user)
                .orElseThrow(() -> new GarageNotFoundException(String.format(GARAGE_NOT_FOUND, user.getUsername())));

        NoteContent content = noteDTO.content();

        GarageCar garageCar = GarageCar
                .builder()
                .owner(user)
                .build();

        Note note = Note
                .builder()
                .noteContent(content)
                .garage(garage)
                .garageCar(garageCar)
                .build();

        if (noteDTO.carId() != null) {
            Car car = carRepository.findById(noteDTO.carId())
                    .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND_BY_ID, noteDTO.carId())));
            if (!car.getGarage().getId().equals(garage.getId())) {
                throw new CarNotInGarageException(CAR_NOT_IN_GARAGE);
            }

            garageCar.setCar(car);
            note.setGarageCar(garageCar);
        }

        noteRepository.save(note);
        garageCarRepository.save(garageCar);

        return noteMapper.mapToNoteResponse(note);
    }

    public NoteResponse updateNote(NoteUpdateRequest noteUpdateRequest){
        Note note = noteRepository.findById(noteUpdateRequest.id())
                .orElseThrow(() -> new NoteNotFoundException(String.format(NOTE_NOT_FOUND, noteUpdateRequest.id())));

        note.setNoteContent(noteUpdateRequest.content());

        noteRepository.save(note);
        return noteMapper.mapToNoteResponse(note);
    }

    public List<NoteResponse> getAllNotes(){
        List<Note> notes = noteRepository.findAll();
        return notes.stream().map(noteMapper::mapToNoteResponse).collect(Collectors.toList());
    }
}
