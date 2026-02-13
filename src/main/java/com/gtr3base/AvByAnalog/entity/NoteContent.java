package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;

@Embeddable
public class NoteContent {

    private String text;

    @Lob
    private byte[] content;
}
