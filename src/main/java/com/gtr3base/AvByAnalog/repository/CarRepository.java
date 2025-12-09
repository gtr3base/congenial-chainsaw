package com.gtr3base.AvByAnalog.repository;

import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findCarById(Long id);

    Optional<Car> findCarByUserId(Integer userId);

    Optional<Car> findByVinCode(String vinCode);

    @Query("SELECT c FROM Car c " +
            "JOIN FETCH c.carModel cm " +
            "JOIN FETCH cm.carMake " +
            "JOIN FETCH c.generation " +
            "WHERE c.status = :status")
    List<Car> findCarsByStatus(@Param("status") CarStatus status);

    @Query("SELECT c FROM Car c " +
            "JOIN FETCH c.carModel cm " +
            "JOIN FETCH cm.carMake " +
            "JOIN FETCH c.generation " +
            "WHERE c.user.id = :userId AND c.status = :status")
    List<Car> findCarsByUserIdAndStatus(@Param("userId")Integer userId,@Param("status") CarStatus status);

    Integer user(User user);
}
