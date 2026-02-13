package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.NoteContent;
import lombok.Builder;

@Builder
public record NoteDTO(
        NoteContent content,
        Long carId
){}
