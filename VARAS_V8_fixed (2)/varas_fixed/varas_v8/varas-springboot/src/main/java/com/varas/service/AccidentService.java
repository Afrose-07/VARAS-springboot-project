package com.varas.service;

import com.varas.exception.*;
import com.varas.model.Accident;
import com.varas.repository.AccidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;

    @Autowired
    private PincodeDataService pincodeDataService;

    // Counter for case IDs (starts at 100 same as original)
    private final AtomicInteger caseCounter = new AtomicInteger(100);

    // ============================================================
    // REPORT ACCIDENT (same logic as original)
    // ============================================================
    @Transactional
    public Accident reportAccident(String reporterName, String vehicleNumber,
                                    String village, String pincode, String severity,
                                    List<String> policeStations, List<String> hospitals) {

        // Validate pincode
        if (!pincodeDataService.isValidPincode(pincode)) {
            throw new InvalidPincodeException(pincode);
        }

        // Check duplicate (same logic as original)
        if (isDuplicateAccident(vehicleNumber, pincode, village)) {
            throw new DuplicateAccidentException(vehicleNumber, pincode, village);
        }

        // Generate case ID (same format as original)
        int counter = caseCounter.incrementAndGet();
        // Sync with DB max to avoid collision on restart
        String caseId = "TIRUNELVELI" + counter;

        String policeStr = String.join(",", policeStations);
        String hospitalStr = String.join(",", hospitals);

        Accident accident = new Accident(caseId, reporterName, vehicleNumber,
                village, pincode, severity, policeStr, hospitalStr);

        return accidentRepository.save(accident);
    }

    // ============================================================
    // DUPLICATE CHECK (same logic as original)
    // ============================================================
    public boolean isDuplicateAccident(String vehicleNumber, String pincode, String village) {
        return accidentRepository.existsByVehicleNumberAndPincodeAndVillageIgnoreCase(
                vehicleNumber, pincode, village);
    }

    // ============================================================
    // GET ALL ACCIDENTS
    // ============================================================
    public List<Accident> getAccidents() {
        return accidentRepository.findAll();
    }

    // ============================================================
    // GET ACCIDENT BY CASE ID
    // ============================================================
    public Accident getAccidentByCaseId(String caseId) {
        return accidentRepository.findByCaseId(caseId)
                .orElseThrow(() -> new AccidentNotFoundException(caseId));
    }

    // ============================================================
    // POLICE CASE METHODS (same logic as original)
    // ============================================================

    // Get all cases for police (simple - shows all cases)
    public List<Map<String, String>> getAllPoliceCases() {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> cases = new ArrayList<>();
        for (Accident a : accidents) {
            Map<String, String> c = new HashMap<>();
            c.put("caseId", a.getCaseId());
            c.put("village", a.getVillage());
            c.put("pincode", a.getPincode());
            c.put("severity", a.getSeverity().toUpperCase());
            c.put("status", a.isPoliceAccepted() ? "ACCEPTED" : "PENDING");
            c.put("acceptedBy", a.isPoliceAccepted() ? a.getAcceptedPolice() : "");
            cases.add(c);
        }
        return cases;
    }

    // Get cases for a specific police station
    public List<Map<String, String>> getCasesForPoliceStation(String policeName) {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> cases = new ArrayList<>();
        for (Accident a : accidents) {
            List<String> stations = parseCommaSeparated(a.getNotifiedPoliceStations());
            if (stations.contains(policeName)) {
                Map<String, String> c = new HashMap<>();
                c.put("caseId", a.getCaseId());
                c.put("village", a.getVillage());
                c.put("pincode", a.getPincode());
                c.put("severity", a.getSeverity().toUpperCase());
                c.put("status", a.isPoliceAccepted() ? "ACCEPTED by " + a.getAcceptedPolice() : "PENDING");
                cases.add(c);
            }
        }
        return cases;
    }

    // Accept police case simple (same logic as original)
    @Transactional
    public boolean acceptPoliceCaseSimple(String caseId) {
        Accident a = accidentRepository.findByCaseId(caseId)
                .orElseThrow(() -> new AccidentNotFoundException(caseId));

        if (a.isPoliceAccepted()) {
            throw new CaseAlreadyAcceptedException(caseId, a.getAcceptedPolice());
        }

        a.setAcceptedPolice("Police Department");
        a.setStatus("Police Dispatched");
        accidentRepository.save(a);
        return true;
    }

    // Accept police case with station name
    @Transactional
    public boolean acceptPoliceCase(String caseId, String policeName) {
        Accident a = accidentRepository.findByCaseId(caseId)
                .orElseThrow(() -> new AccidentNotFoundException(caseId));

        List<String> stations = parseCommaSeparated(a.getNotifiedPoliceStations());
        if (!stations.contains(policeName)) {
            throw new IllegalArgumentException("Your police station was not notified for this case.");
        }

        if (a.isPoliceAccepted()) {
            throw new CaseAlreadyAcceptedException(caseId, a.getAcceptedPolice());
        }

        a.setAcceptedPolice(policeName);

        if (a.isHospitalAccepted()) {
            a.setStatus("Police Dispatched + Ambulance Dispatched");
        } else {
            a.setStatus("Police Dispatched - Waiting for Hospital");
        }

        accidentRepository.save(a);
        return true;
    }

    // ============================================================
    // HOSPITAL CASE METHODS (same logic as original)
    // ============================================================

    // Get all cases for hospital (simple)
    public List<Map<String, String>> getAllHospitalCases() {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> cases = new ArrayList<>();
        for (Accident a : accidents) {
            Map<String, String> c = new HashMap<>();
            c.put("caseId", a.getCaseId());
            c.put("village", a.getVillage());
            c.put("pincode", a.getPincode());
            c.put("severity", a.getSeverity().toUpperCase());
            c.put("status", a.isHospitalAccepted() ? "ACCEPTED" : "PENDING");
            c.put("acceptedBy", a.isHospitalAccepted() ? a.getAcceptedHospital() : "");
            cases.add(c);
        }
        return cases;
    }

    // Get cases for a specific hospital
    public List<Map<String, String>> getCasesForHospital(String hospitalName) {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> cases = new ArrayList<>();
        for (Accident a : accidents) {
            List<String> hospitals = parseCommaSeparated(a.getNotifiedHospitals());
            if (hospitals.contains(hospitalName)) {
                Map<String, String> c = new HashMap<>();
                c.put("caseId", a.getCaseId());
                c.put("village", a.getVillage());
                c.put("pincode", a.getPincode());
                c.put("severity", a.getSeverity().toUpperCase());
                c.put("status", a.isHospitalAccepted() ? "ACCEPTED by " + a.getAcceptedHospital() : "PENDING");
                cases.add(c);
            }
        }
        return cases;
    }

    // Accept hospital case simple (same logic as original)
    @Transactional
    public boolean acceptHospitalCaseSimple(String caseId) {
        Accident a = accidentRepository.findByCaseId(caseId)
                .orElseThrow(() -> new AccidentNotFoundException(caseId));

        if (a.isHospitalAccepted()) {
            throw new CaseAlreadyAcceptedException(caseId, a.getAcceptedHospital());
        }

        a.setAcceptedHospital("Hospital Department");
        if (a.isPoliceAccepted()) {
            a.setStatus("Police + Ambulance Dispatched");
        } else {
            a.setStatus("Ambulance Dispatched");
        }

        accidentRepository.save(a);
        return true;
    }

    // Accept hospital case with hospital name
    @Transactional
    public boolean acceptHospitalCase(String caseId, String hospitalName) {
        Accident a = accidentRepository.findByCaseId(caseId)
                .orElseThrow(() -> new AccidentNotFoundException(caseId));

        List<String> hospitals = parseCommaSeparated(a.getNotifiedHospitals());
        if (!hospitals.contains(hospitalName)) {
            throw new IllegalArgumentException("Your hospital was not notified for this case.");
        }

        if (a.isHospitalAccepted()) {
            throw new CaseAlreadyAcceptedException(caseId, a.getAcceptedHospital());
        }

        a.setAcceptedHospital(hospitalName);

        if (a.isPoliceAccepted()) {
            a.setStatus("Police Dispatched + Ambulance Dispatched");
        } else {
            a.setStatus("Ambulance Dispatched - Waiting for Police");
        }

        accidentRepository.save(a);
        return true;
    }

    // ============================================================
    // ADMIN SEARCH METHODS (same logic as original)
    // ============================================================

    public List<Accident> getAccidentsByDate(String date) {
        // date format: dd/MM/yyyy (same as original)
        return accidentRepository.findAll().stream()
                .filter(a -> a.getDateOnly().equals(date))
                .collect(Collectors.toList());
    }

    public List<Accident> getAccidentsByPincode(String pincode) {
        return accidentRepository.findByPincode(pincode);
    }

    public List<Accident> getAccidentsBySeverity(String severity) {
        return accidentRepository.findBySeverityIgnoreCase(severity);
    }

    // ============================================================
    // STATISTICS (same logic as original)
    // ============================================================

    public int getTotalAccidents() {
        return (int) accidentRepository.count();
    }

    public int getHighSeverityCount() {
        return (int) accidentRepository.countBySeverityIgnoreCase("high");
    }

    public int getMediumSeverityCount() {
        return (int) accidentRepository.countBySeverityIgnoreCase("medium");
    }

    public int getLowSeverityCount() {
        return (int) accidentRepository.countBySeverityIgnoreCase("low");
    }

    public int getAcceptedPoliceCount() {
        return (int) accidentRepository.countPoliceAccepted();
    }

    public int getAcceptedHospitalCount() {
        return (int) accidentRepository.countHospitalAccepted();
    }


    // ============================================================
    // HISTORY: Accepted cases for a hospital
    // ============================================================
    public List<Map<String, String>> getAcceptedCasesForHospital(String hospitalName) {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> result = new ArrayList<>();
        for (Accident a : accidents) {
            if (hospitalName.equals(a.getAcceptedHospital())) {
                Map<String, String> c = new HashMap<>();
                c.put("caseId", a.getCaseId());
                c.put("village", a.getVillage());
                c.put("pincode", a.getPincode());
                c.put("severity", a.getSeverity().toUpperCase());
                c.put("status", a.getStatus());
                c.put("reportedAt", a.getFormattedDate());
                c.put("vehicle", a.getVehicleNumber());
                result.add(c);
            }
        }
        return result;
    }

    // ============================================================
    // HISTORY: Accepted cases for a police station
    // ============================================================
    public List<Map<String, String>> getAcceptedCasesForPoliceStation(String stationName) {
        List<Accident> accidents = accidentRepository.findAll();
        List<Map<String, String>> result = new ArrayList<>();
        for (Accident a : accidents) {
            if (stationName.equals(a.getAcceptedPolice())) {
                Map<String, String> c = new HashMap<>();
                c.put("caseId", a.getCaseId());
                c.put("village", a.getVillage());
                c.put("pincode", a.getPincode());
                c.put("severity", a.getSeverity().toUpperCase());
                c.put("status", a.getStatus());
                c.put("reportedAt", a.getFormattedDate());
                c.put("vehicle", a.getVehicleNumber());
                result.add(c);
            }
        }
        return result;
    }

    // ============================================================
    // GET NOTIFIED HOSPITALS (only those in actual cases)
    // ============================================================
    public List<String> getNotifiedHospitals() {
        List<Accident> accidents = accidentRepository.findAll();
        return accidents.stream()
            .flatMap(a -> parseCommaSeparated(a.getNotifiedHospitals()).stream())
            .filter(h -> h != null && !h.isBlank())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    // ============================================================
    // GET NOTIFIED POLICE STATIONS (only those in actual cases)
    // ============================================================
    public List<String> getNotifiedPoliceStations() {
        List<Accident> accidents = accidentRepository.findAll();
        return accidents.stream()
            .flatMap(a -> parseCommaSeparated(a.getNotifiedPoliceStations()).stream())
            .filter(p -> p != null && !p.isBlank())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    // ============================================================
    // HELPER
    // ============================================================
    private List<String> parseCommaSeparated(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();
        return Arrays.asList(value.split(","));
    }
}
