package com.gtr3base.AvByAnalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtr3base.AvByAnalog.controller.CarController;
import com.gtr3base.AvByAnalog.dto.CarDTO;
import com.gtr3base.AvByAnalog.dto.CarResponse;
import com.gtr3base.AvByAnalog.dto.CarSearchFilter;
import com.gtr3base.AvByAnalog.dto.CarStatusUpdateDto;
import com.gtr3base.AvByAnalog.enums.CarAction;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import com.gtr3base.AvByAnalog.mappers.CarFromRequestMapper;
import com.gtr3base.AvByAnalog.repository.CarModelRepository;
import com.gtr3base.AvByAnalog.repository.CarRepository;
import com.gtr3base.AvByAnalog.security.JwtAuthFilter;
import com.gtr3base.AvByAnalog.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CarController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private CarRepository carRepository;

    @MockitoBean
    private CarModelRepository carModelRepository;

    @MockitoBean
    private CarFromRequestMapper carFromRequestMapper;

    private CarDTO carRequest;
    private CarResponse carResponse;
    private CarStatusUpdateDto statusUpdateDto;


    @BeforeEach
    void setUp() {
        carRequest = new CarDTO(
                1L,
                1L,
                1L,
                2020,
                new BigDecimal("15000.00"),
                "Great condition",
                "ABC12345678901234"
        );

        carResponse = CarResponse.builder()
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

        statusUpdateDto = new CarStatusUpdateDto(CarStatus.APPROVED);
    }

    @Test
    void addCar_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        when(carService.createCar(any(CarDTO.class), any()))
                .thenReturn(carResponse);

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carModel").value("F30 3-series"))
                .andExpect(jsonPath("$.carMake").value("BMW"))
                .andExpect(jsonPath("$.vinCode").value("ABC12345678901234"))
                .andExpect(jsonPath("$.carStatus").value(CarStatus.PENDING.name()));
    }

    @Test
    void addCar_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        CarDTO invalidRequest = new CarDTO(
                1L,
                1L,
                1L,
                2020,
                new BigDecimal("15000.00"),
                "Great condition",
                null
        );

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCarById_ShouldReturnCar_WhenIdExists() throws Exception {
        when(carService.getCarById(1L)).thenReturn(carResponse);

        mockMvc.perform(get("/api/cars/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.carModel").value("F30 3-series"));
    }

    @Test
    void getCarTransitionById_ShouldReturnStatusArray() throws Exception {
        CarStatus[] transitions = {CarStatus.APPROVED, CarStatus.REJECTED};

        when(carService.getAvailableTransitions(1L)).thenReturn(transitions);

        mockMvc.perform(get("/api/cars/admin/transition/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(CarStatus.APPROVED.name()))
                .andExpect(jsonPath("$[1]").value(CarStatus.REJECTED.name()));
    }

    @Test
    void approveCarById_ShouldReturnUpdatedCar_WhenValid() throws Exception {
        CarResponse approvedResponse = CarResponse.builder()
                .id(1L)
                .carStatus(CarStatus.APPROVED)
                .build();

        when(carService.updateCarStatus(eq(1L), eq(CarStatus.APPROVED)))
                .thenReturn(approvedResponse);

        mockMvc.perform(put("/api/cars/admin/status/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(statusUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carStatus").value("APPROVED"));
    }

    @Test
    void approveCarById_ShouldReturnBadRequest_WhenBodyMissing() throws Exception {
        mockMvc.perform(put("/api/cars/admin/status/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_ShouldReturnUpdatedCar_WhenRequestIsValid() throws Exception {
        CarResponse updatedResponse = CarResponse.builder()
                .id(1L)
                .carMake("BMW")
                .description("Updated Description")
                .price(new BigDecimal("14000.00"))
                .vinCode("ABC12345678901234")
                .build();

        when(carService.updateCar(eq(1L), any(CarDTO.class), any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/cars/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value("14000.0"));
    }

    @Test
    void searchCars_ShouldReturnListOfCars_WhenRequestIsValid() throws Exception {
        CarResponse response = new CarResponse();
        response.setId(1L);
        response.setCarMake("BMW");

        List<CarResponse> responses = List.of(response);

        when(carService.searchCars(any(CarSearchFilter.class), any())).thenReturn(responses);

        mockMvc.perform(get("/api/cars/search")
                        .param("status", "APPROVED")
                        .param("carMake", "BMW")
                        .param("minPrice", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].carMake").value("BMW"));

        ArgumentCaptor<CarSearchFilter> filterCaptor = ArgumentCaptor.forClass(CarSearchFilter.class);

        verify(carService).searchCars(filterCaptor.capture(), any());

        CarSearchFilter capturedFilter = filterCaptor.getValue();

        assertEquals(CarStatus.APPROVED, capturedFilter.getStatus());
        assertEquals("BMW", capturedFilter.getCarMake());
    }

    @Test
    void deleteCar_ShouldReturnCar_WhenRequestIsValid() throws Exception {
        CarResponse deletedCar = new CarResponse();

        deletedCar.setId(1L);
        deletedCar.setCarAction(CarAction.DELETE);

        when(carService.deleteCar(eq(1L), any())).thenReturn(deletedCar);

        mockMvc.perform(delete("/api/cars/delete/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.carAction").value("DELETE"));
    }
}
