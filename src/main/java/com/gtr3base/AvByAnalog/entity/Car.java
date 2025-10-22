package com.gtr3base.AvByAnalog.entity;

import com.gtr3base.AvByAnalog.enums.CarStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "generation_id")
    private Integer generationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id", insertable = false, updatable = false)
    private CarGeneration generation;

    @Column(name = "year")
    private Integer year;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status = CarStatus.PENDING;

    @Column(name = "vin_code",nullable = false,length = 17)
    private String vinCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", userId=" + userId +
                ", generationId=" + generationId +
                ", year=" + year +
                ", price=" + price +
                ", status=" + status +
                ", vinCode='" + vinCode + '\'' +
                '}';
    }
}
