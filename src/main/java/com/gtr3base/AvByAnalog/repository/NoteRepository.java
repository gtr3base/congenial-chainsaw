package com.gtr3base.AvByAnalog.repository;

import com.gtr3base.AvByAnalog.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findAllByGarageCarOwnerId(Integer garageCarOwnerId);
}
