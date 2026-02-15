package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.NoteContent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoteResponse(
        Long id,
        NoteContent content,
        Long garageId,
        Long carId,
        LocalDateTime createdAt
) {}
