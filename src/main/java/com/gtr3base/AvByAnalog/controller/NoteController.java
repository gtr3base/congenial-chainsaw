package com.gtr3base.AvByAnalog.controller;

import com.gtr3base.AvByAnalog.dto.NoteDTO;
import com.gtr3base.AvByAnalog.dto.NoteResponse;
import com.gtr3base.AvByAnalog.dto.NoteUpdateRequest;
import com.gtr3base.AvByAnalog.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteDTO noteDTO){
        return ResponseEntity.ok(noteService.createNote(noteDTO));
    }

    @PutMapping
    public ResponseEntity<NoteResponse> updateNote(@RequestBody NoteUpdateRequest noteUpdateRequest){
        return ResponseEntity.ok(noteService.updateNote(noteUpdateRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NoteResponse>> getAllNotes(){
        return ResponseEntity.ok(noteService.getAllNotes());
    }
}
