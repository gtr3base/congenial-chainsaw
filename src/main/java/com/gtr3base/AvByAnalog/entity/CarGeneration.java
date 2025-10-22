package com.gtr3base.AvByAnalog.entity;

import jakarta.persistence.*;
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

    @Column(name = "model_id", nullable = false)
    private Integer modelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private CarModel carModel;

    @Column(name = "year_start", nullable = false)
    private Integer yearStart;

    @Column(name = "year_end")
    private Integer yearEnd;
    
    @Column(name = "name", nullable = false, length = 50)
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
                ", modelId=" + modelId +
                ", yearStart=" + yearStart +
                ", yearEnd=" + yearEnd +
                ", name='" + name + '\'' +
                '}';
    }
}
