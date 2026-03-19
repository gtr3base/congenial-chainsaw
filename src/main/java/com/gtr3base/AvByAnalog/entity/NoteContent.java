package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteContent {
    private String title;
    private String text;

    @Lob
    private byte[] content;
}
