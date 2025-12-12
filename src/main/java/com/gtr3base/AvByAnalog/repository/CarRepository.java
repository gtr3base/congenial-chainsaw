package com.gtr3base.AvByAnalog.repository;

import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    Optional<Car> findCarById(Long id);

    Optional<Car> findCarByUserId(Integer userId);

    Optional<Car> findByVinCode(String vinCode);

    Integer user(User user);
}
