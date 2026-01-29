package com.gtr3base.AvByAnalog;

import com.gtr3base.AvByAnalog.controller.AverageCarPriceController;
import com.gtr3base.AvByAnalog.dto.AverageCarPriceSearchFilter;
import com.gtr3base.AvByAnalog.security.JwtAuthFilter;
import com.gtr3base.AvByAnalog.service.AverageCarPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AverageCarPriceController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class AverageCarPriceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AverageCarPriceService averageCarPriceService;

    @Test
    void searchCars_ShouldReturnAverageCarPrice_WhenRequestIsValid() throws Exception {
        BigDecimal response = new BigDecimal("12850.99");

        when(averageCarPriceService.getAverageCarPrice(any(AverageCarPriceSearchFilter.class))).thenReturn(response);

        mockMvc.perform(get("/api/average-car-price")
                    .param("carModel", "F30 3-series")
                    .param("carMake", "BMW")
                    .param("year", "2024"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").value(response));

        verify(averageCarPriceService, times(1)).getAverageCarPrice(any(AverageCarPriceSearchFilter.class));
    }
}
