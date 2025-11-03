package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite {
    @EmbeddedId
    @NotNull(message = "Favorite ID is required")
    @Valid
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", insertable = false, updatable = false)
    @NotNull(message = "Car is required")
    private Car car;

    @Column(name = "added_at")
    @PastOrPresent(message = "Creation cannot be in the future")
    private LocalDateTime addedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }

    public Integer getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public Integer getCarId() {
        return id != null ? id.getCarId() : null;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "userId=" + getUserId() +
                ", carId=" + getCarId() +
                ", addedAt=" + addedAt +
                '}';
    }
}
