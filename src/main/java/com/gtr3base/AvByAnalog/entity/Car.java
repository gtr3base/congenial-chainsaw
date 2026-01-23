package com.gtr3base.AvByAnalog.entity;

import com.gtr3base.AvByAnalog.annotations.ValidGenerationYear;
import com.gtr3base.AvByAnalog.annotations.ValidYear;
import com.gtr3base.AvByAnalog.enums.CarAction;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ValidGenerationYear(
        yearField = "year",
        generationField = "generation",
        message = "The year is not valid for the selected car generation"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceTracking> priceHistory;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;

    @NotNull(message = "Car generation is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private CarGeneration generation;

    @Min(value = 1886, message = "Year must be after 1886")
    @ValidYear(message = "Year cannot be more than current + 1")
    @Column(name = "year")
    private Integer year;

    @DecimalMin(value = "0.0", message = "Price cant be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status = CarStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private CarAction pendingAction;

    @NotBlank(message = "VIN code is required")
    @Size(min = 17, max = 17, message = "VIN code must be exactly 17 characters")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN code format")
    @Column(name = "vin_code",nullable = false,length = 17)
    private String vinCode;

    @Column(name = "created_at")
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull(message = "Car Model is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModel carModel;

    public boolean isValidYearForGeneration(){
        if(generation == null){
            return true;
        }

        int currentYear = LocalDate.now().getYear();
        int effectiveYearEnd = generation.getYearEnd() != null ?
                generation.getYearEnd() : currentYear;

        return year >= generation.getYearStart() && year <= effectiveYearEnd;
    }

    public boolean isPending() {
        return CarStatus.PENDING.equals(status);
    }

    public boolean isApproved() {
        return CarStatus.APPROVED.equals(status);
    }

    public boolean isRejected() {
        return CarStatus.REJECTED.equals(status);
    }



    @PrePersist
    @PreUpdate
    public void validate(){
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if(vinCode == null || vinCode.length() != 17){
            throw new IllegalArgumentException("VinCode must be 17 characters");
        }

        this.updatedAt = LocalDateTime.now();
    }
}
