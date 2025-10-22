package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "car_models",
        uniqueConstraints = @UniqueConstraint(columnNames = {"make_id", "name"})
)
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "make_id", nullable = false)
    private Integer makeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "make_id", insertable = false, updatable = false)
    private CarMake carMake;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "carModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarGeneration> generations = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceTracking> priceHistory = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();
}
