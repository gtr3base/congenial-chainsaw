package com.gtr3base.AvByAnalog.entity;

import com.gtr3base.AvByAnalog.annotations.ValidYear;
import jakarta.persistence.Entity;
import  jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.PostLoad;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "car_generations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"model_id", "name"})
)
public class CarGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Car model is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private CarModel carModel;

    @Min(value = 1886, message = "Year must be after 1886")
    @ValidYear(message = "Year cannot be more than current + 1")
    @Column(name = "year_start", nullable = false)
    private Integer yearStart;

    @ValidYear(message = "Year cannot be more than current + 1")
    @Column(name = "year_end")
    private Integer yearEnd;

    @NotBlank(message = "Car name is required")
    @Column(name = "name", nullable = false, length = 50)
    @Size(max = 50, message = "Max name length is 50")
    private String name;

    public boolean isCurrentGeneration(){
        return yearEnd == null;
    }

    public String getYearRange(){
        if(yearEnd == null){
            return yearStart + "-Present";
        }
        else{
            return yearStart + "-" + yearEnd;
        }
    }

    @PostLoad
    @PrePersist
    @PreUpdate
    private void validateYearRange(){
        if(yearEnd != null && yearEnd <= yearStart){
            throw new IllegalArgumentException("year end must be greater (or equal) than year start");
        }
    }

    @Override
    public String toString() {
        return "CarGeneration{" +
                "id=" + id +
                ", modelId=" + carModel.getId() +
                ", yearStart=" + yearStart +
                ", yearEnd=" + yearEnd +
                ", name='" + name + '\'' +
                '}';
    }
}
