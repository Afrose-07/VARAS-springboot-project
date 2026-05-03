package com.varas.repository;

import com.varas.model.Accident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccidentRepository extends JpaRepository<Accident, String> {

    // Find by case ID
    Optional<Accident> findByCaseId(String caseId);

    // Check for duplicate accident (same logic as original)
    boolean existsByVehicleNumberAndPincodeAndVillageIgnoreCase(
            String vehicleNumber, String pincode, String village);

    // Find by pincode
    List<Accident> findByPincode(String pincode);

    // Find by severity (case-insensitive)
    List<Accident> findBySeverityIgnoreCase(String severity);

    // Count by severity
    long countBySeverityIgnoreCase(String severity);

    // Custom query: find by date (formatted as dd/MM/yyyy stored in SQLite)
    // We query on reportDateTime; service filters date-only match
    @Query("SELECT a FROM Accident a WHERE a.reportDateTime IS NOT NULL")
    List<Accident> findAll();

    // Count police accepted
    @Query("SELECT COUNT(a) FROM Accident a WHERE a.acceptedPolice IS NOT NULL AND a.acceptedPolice <> ''")
    long countPoliceAccepted();

    // Count hospital accepted
    @Query("SELECT COUNT(a) FROM Accident a WHERE a.acceptedHospital IS NOT NULL AND a.acceptedHospital <> ''")
    long countHospitalAccepted();
}
