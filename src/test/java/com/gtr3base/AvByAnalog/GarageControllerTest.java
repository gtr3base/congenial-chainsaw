package com.gtr3base.AvByAnalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtr3base.AvByAnalog.controller.GarageController;
import com.gtr3base.AvByAnalog.dto.CarCreateRequest;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoDTO;
import com.gtr3base.AvByAnalog.dto.GarageInfoResponse;
import com.gtr3base.AvByAnalog.dto.NoteResponse;
import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.CarModel;
import com.gtr3base.AvByAnalog.entity.Note;
import com.gtr3base.AvByAnalog.entity.NoteContent;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import com.gtr3base.AvByAnalog.exceptions.CarNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.GarageNotFoundException;
import com.gtr3base.AvByAnalog.security.JwtAuthFilter;
import com.gtr3base.AvByAnalog.service.GarageService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = GarageController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class GarageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GarageService garageService;

    private GarageInfoResponse garageInfoResponse;

    private NoteResponse noteResponse;

    private Note note;

    private Car car;

    private NoteContent content;

    private CarDTO carDTO;

    private GarageInfoDTO garageInfoDTO;

    private CarCreateRequest carCreateRequest;

    @BeforeEach
    public void setup() throws Exception{
        byte[] b = new byte[1];
        b[0] = 1;

        note = Note
                .builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .build();

        CarModel carModel = new CarModel();
        carModel.setName("BMW X5");
        carModel.setId(1);

        car = Car
                .builder()
                .id(1L)
                .carModel(carModel)
                .build();

        content = NoteContent
                .builder().text("Note text example")
                .content(b)
                .build();

        noteResponse = NoteResponse
                .builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .carId(1L)
                .garageId(1L)
                .content(content)
                .build();

        carDTO = CarDTO.builder()
                .id(1L)
                .carMake("BMW")
                .carModel("F30 3-series")
                .carGeneration("6th-gen")
                .carStatus(CarStatus.PENDING)
                .price(new BigDecimal("15000.00"))
                .year(2018)
                .description("Great condition")
                .userId(1L)
                .username("Test")
                .vinCode("ABC12345678901234")
                .build();

        garageInfoResponse = GarageInfoResponse.builder()
                .garageId(1L)
                .notes(List.of(noteResponse))
                .car(carDTO)
                .locked(true)
                .build();

        garageInfoDTO = GarageInfoDTO.builder()
                .content(content)
                .garageId(1L)
                .noteId(1L)
                .carId(1L)
                .locked(true)
                .build();

        carCreateRequest = CarCreateRequest.builder()
                .generationId(1L)
                .makeId(1L)
                .modelId(1L)
                .generationId(1L)
                .year(2018)
                .description("Great condition")
                .price(new BigDecimal("15000.00"))
                .vinCode("ABC12345678901234")
                .build();
    }

    @Test
    void addGarage_ShouldReturnGarageInfoResponse() throws Exception {
        when(garageService.addGarage(any(GarageInfoDTO.class)))
                .thenReturn(garageInfoResponse);

        mockMvc.perform(post("/api/garage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageInfoDTO)))
                .andDo(print())
                .andExpect(jsonPath("$.garageId").value(1))
                .andExpect(jsonPath("$.locked").value(true))
                .andExpect(jsonPath("$.notes[0].id").value(1))
                .andExpect(jsonPath("$.car.id").value(1))
                .andExpect(jsonPath("$.car.carMake").value("BMW"));

    }
    @Test
    void getMyGarage_ShouldReturnGarageInfoResponse() throws Exception {
        when(garageService.getGarageByUserId())
                .thenReturn(garageInfoResponse);

        mockMvc.perform(get("/api/garage"))
                .andDo(print())
                .andExpect(jsonPath("$.garageId").value(1))
                .andExpect(jsonPath("$.locked").value(true))
                .andExpect(jsonPath("$.notes[0].id").value(1))
                .andExpect(jsonPath("$.car.id").value(1))
                .andExpect(jsonPath("$.car.carMake").value("BMW"));
}

    @Test
    void updateGarage_shouldReturnGarageInfoResponse() throws Exception {
        when(garageService.updateGarage(any(GarageInfoDTO.class)))
                .thenReturn(garageInfoResponse);

        mockMvc.perform(put("/api/garage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageInfoDTO)))
                .andDo(print())
                .andExpect(jsonPath("$.garageId").value(1))
                .andExpect(jsonPath("$.locked").value(true))
                .andExpect(jsonPath("$.notes[0].id").value(1))
                .andExpect(jsonPath("$.car.id").value(1))
                .andExpect(jsonPath("$.car .carMake").value("BMW"));
    }

    @Test
    void deleteGarage_shouldReturnGarageInfoResponse() throws Exception {
        mockMvc.perform(delete("/api/garage/{id}",1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addCarToGarage_shouldReturnGarageInfoResponse() throws Exception {
        mockMvc.perform(post("/api/garage/add-car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addGarage_shouldThrowCarNotFound_WhenCarIdInvalid() throws Exception {
        String errorMessage = "Car with ID 1 not found";
        when(garageService.addGarage(any(GarageInfoDTO.class)))
                .thenThrow(new CarNotFoundException(errorMessage));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/garage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(garageInfoDTO)));
        });

        assertInstanceOf(CarNotFoundException.class, exception.getCause());
    }

    @Test
    void getMyGarage_shouldThrowGarageNotFound_WhenUserHasNoGarage() throws Exception {
        when(garageService.getGarageByUserId())
                .thenThrow(new GarageNotFoundException("Garage not found for user"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/garage"));
        });

        assertInstanceOf(GarageNotFoundException.class, exception.getCause());
    }

    @Test
    void updateGarage_shouldThrowGarageNotFound_WhenGarageIdInvalid() throws Exception {
        when(garageService.updateGarage(any(GarageInfoDTO.class)))
                .thenThrow(new GarageNotFoundException("Garage not found"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/api/garage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(garageInfoDTO)));
        });

        assertInstanceOf(GarageNotFoundException.class, exception.getCause());
    }

    @Test
    void updateGarage_shouldThrowCarNotFound_WhenCarToAddInvalid() throws Exception {
        when(garageService.updateGarage(any(GarageInfoDTO.class)))
                .thenThrow(new CarNotFoundException("Car not found"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/api/garage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(garageInfoDTO)));
        });

        assertInstanceOf(CarNotFoundException.class, exception.getCause());
    }
}
