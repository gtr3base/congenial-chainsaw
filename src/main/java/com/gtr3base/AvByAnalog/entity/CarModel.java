package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.Entity;
import  jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Car make is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "make_id", insertable = false, updatable = false)
    private CarMake carMake;

    @NotBlank(message = "Car name is required")
    @Column(name = "name", nullable = false, length = 50)
    @Size(max = 50, message = "Model name max length must be 50")
    private String name;

    @OneToMany(mappedBy = "carModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @Valid
    private List<CarGeneration> generations = new ArrayList<>();
}
