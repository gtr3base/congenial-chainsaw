package com.gtr3base.AvByAnalog.repository;

import com.gtr3base.AvByAnalog.entity.Garage;
import com.gtr3base.AvByAnalog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage,Long> {
    Optional<Garage> findByUser(User user);
}
