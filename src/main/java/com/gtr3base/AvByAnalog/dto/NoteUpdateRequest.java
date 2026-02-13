package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.NoteContent;

public record NoteUpdateRequest(
        Long id,
        NoteContent content
){}
