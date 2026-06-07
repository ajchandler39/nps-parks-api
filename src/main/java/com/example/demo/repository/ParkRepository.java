package com.example.demo.repository;

import com.example.demo.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkRepository extends JpaRepository<Park, String> {

    Optional<Park> findByParkCode(String parkCode);

    List<Park> findByStatesContaining(String state);

    List<Park> findByDesignation(String designation);
}