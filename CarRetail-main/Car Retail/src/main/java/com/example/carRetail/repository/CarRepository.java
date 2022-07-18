package com.example.carRetail.repository;

import com.example.carRetail.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findAllByDeleteStatus(boolean b);
    Car findByRegNumber(String regNumber);

}
