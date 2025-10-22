package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteId implements Serializable {
    private Integer userId;
    private Integer carId;
}