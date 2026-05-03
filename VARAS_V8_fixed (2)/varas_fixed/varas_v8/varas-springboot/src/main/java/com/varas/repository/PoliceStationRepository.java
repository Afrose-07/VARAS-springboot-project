package com.varas.repository;

import com.varas.model.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStation, Long> {
    Optional<PoliceStation> findByNameIgnoreCase(String name);
    List<PoliceStation> findByPincodesContaining(String pincode);
    boolean existsByNameIgnoreCase(String name);
}
