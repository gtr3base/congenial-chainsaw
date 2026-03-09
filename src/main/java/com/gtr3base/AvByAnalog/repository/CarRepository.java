package com.gtr3base.AvByAnalog.repository;

import com.gtr3base.AvByAnalog.entity.Car;
import com.gtr3base.AvByAnalog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends PagingAndSortingRepository<Car, Long>, JpaSpecificationExecutor<Car>, JpaRepository<Car, Long> {

    Optional<Car> findCarById(Long id);

    Optional<List<Car>> findCarsByUserId(Integer userId);

    Long user(User user);

    Optional<List<Car>> findCarsById(Long id);
}
