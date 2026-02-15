package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.NoteContent;
import lombok.Builder;

@Builder
public record NoteUpdateRequest(
        Long id,
        NoteContent content
){}
