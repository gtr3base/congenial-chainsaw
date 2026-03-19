package com.gtr3base.AvByAnalog.dto;

import com.gtr3base.AvByAnalog.entity.NoteContent;
import lombok.Builder;

@Builder
public record GarageInfoDTO(
        Boolean locked,
        NoteContent content,
        Long noteId,
        Long carId,
        Long garageId
){}
