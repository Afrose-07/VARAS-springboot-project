package com.varas.repository;

import com.varas.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByNameIgnoreCase(String name);
    List<Hospital> findByPincodesContaining(String pincode);
    boolean existsByNameIgnoreCase(String name);
}
