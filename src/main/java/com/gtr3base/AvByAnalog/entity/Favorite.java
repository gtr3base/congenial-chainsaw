package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.*;
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
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", insertable = false, updatable = false)
    private Car car;

    @Column(name = "added_at")
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
