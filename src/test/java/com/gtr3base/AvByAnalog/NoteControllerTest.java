package com.gtr3base.AvByAnalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtr3base.AvByAnalog.controller.NoteController;
import com.gtr3base.AvByAnalog.dto.NoteDTO;
import com.gtr3base.AvByAnalog.dto.NoteResponse;
import com.gtr3base.AvByAnalog.dto.NoteUpdateRequest;
import com.gtr3base.AvByAnalog.entity.NoteContent;
import com.gtr3base.AvByAnalog.exceptions.CarNotFoundException;
import com.gtr3base.AvByAnalog.exceptions.CarNotInGarageException;
import com.gtr3base.AvByAnalog.exceptions.NoteNotFoundException;
import com.gtr3base.AvByAnalog.security.JwtAuthFilter;
import com.gtr3base.AvByAnalog.service.NoteService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
        controllers = NoteController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    private NoteResponse noteResponse;

    private NoteDTO noteDTO;

    private NoteUpdateRequest noteUpdateRequest;

    @BeforeEach
    public void setup(){
        byte[] b = new byte[1];
        b[0] = 1;

        NoteContent content = NoteContent
                .builder()
                .text("Note text example")
                .content(b)
                .build();

        noteResponse = NoteResponse
                .builder()
                .carId(1L)
                .garageId(1L)
                .content(content)
                .build();

        noteDTO = NoteDTO
                .builder()
                .content(content)
                .carId(1L)
                .build();

        noteUpdateRequest = NoteUpdateRequest
                .builder()
                .content(content)
                .id(1L)
                .build();

    }

    @Test
    void createNote_shouldReturnNoteResponseWhenValid() throws Exception {
        when(noteService.createNote(any(NoteDTO.class)))
                .thenReturn(noteResponse);
        mockMvc.perform(post("/api/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)))
                .andDo(print())
                .andExpect(jsonPath("$.carId").value(1L))
                .andExpect(jsonPath("$.garageId").value(1L))
                .andExpect(jsonPath("$.content.text").value("Note text example"));
    }

    @Test
    void updateNote_shouldReturnNoteResponseWhenValid() throws Exception {
        when(noteService.updateNote(any(NoteUpdateRequest.class)))
                .thenReturn(noteResponse);

        mockMvc.perform(put("/api/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteUpdateRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.content.text").value("Note text example"));
    }

    @Test
    void getAllNotes_ShouldReturnNotesWhenValid() throws Exception {
        when(noteService.getAllNotes())
                .thenReturn(List.of(noteResponse));

        mockMvc.perform(get("/api/note/all"))
                .andExpect(jsonPath("$[0].content.text").value("Note text example"));
    }

    @Test
    void createNote_shouldThrowCarNotFound_WhenCarIdInvalid() throws Exception {
        String errorMessage = "Car with ID 1 not found";
        when(noteService.createNote(any(NoteDTO.class)))
                .thenThrow(new CarNotFoundException(errorMessage));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/note")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noteDTO)));
        });

        assertInstanceOf(CarNotFoundException.class, exception.getCause());
    }

    @Test
    void createNote_shouldThrowCarNotInGarage_WhenCarBelongsToOtherGarage() throws Exception {
        when(noteService.createNote(any(NoteDTO.class)))
                .thenThrow(new CarNotInGarageException("Car not in garage"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/note")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noteDTO)));
        });

        assertInstanceOf(CarNotInGarageException.class, exception.getCause());
    }

    @Test
    void updateNote_shouldThrowNoteNotFound_WhenIdInvalid() throws Exception {
        when(noteService.updateNote(any(NoteUpdateRequest.class)))
                .thenThrow(new NoteNotFoundException("Note not found"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/api/note")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noteUpdateRequest)));
        });

        assertInstanceOf(NoteNotFoundException.class, exception.getCause());
    }
}
