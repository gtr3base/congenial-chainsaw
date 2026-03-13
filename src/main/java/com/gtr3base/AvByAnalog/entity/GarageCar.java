package com.gtr3base.AvByAnalog.entity;

import com.gtr3base.AvByAnalog.annotations.ValidYear;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "garage_car")
public class GarageCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Garage is required")
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @Min(value = 1886, message = "Year must be after 1886")
    @ValidYear(message = "Year cannot be more than current + 1")
    @Column(name = "year")
    private Integer year;

    @NotNull(message = "Car generation is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private CarGeneration generation;

    @NotBlank(message = "VIN code is required")
    @Size(min = 17, max = 17, message = "VIN code must be exactly 17 characters")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN code format")
    @Column(name = "vin_code",nullable = false,length = 17)
    private String vinCode;

    @DecimalMin(value = "0.0", message = "Price cant be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Car Model is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModel carModel;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;
}
