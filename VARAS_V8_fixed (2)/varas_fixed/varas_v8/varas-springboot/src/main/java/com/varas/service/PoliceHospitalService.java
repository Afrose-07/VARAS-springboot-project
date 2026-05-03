package com.varas.service;

import com.varas.model.Hospital;
import com.varas.model.PoliceStation;
import com.varas.repository.HospitalRepository;
import com.varas.repository.PoliceStationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PoliceHospitalService {

    @Autowired
    private PoliceStationRepository policeStationRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    // Seed initial data if tables are empty
    @PostConstruct
    public void seedData() {
        if (policeStationRepository.count() == 0) {
            seedPoliceStations();
        }
        if (hospitalRepository.count() == 0) {
            seedHospitals();
        }
    }

    private void seedPoliceStations() {
        List<PoliceStation> stations = new ArrayList<>();
        // Tirunelveli City Police Stations
        stations.add(new PoliceStation("Tirunelveli Junction Police Station", "627001"));
        stations.add(new PoliceStation("Palayamkottai Police Station", "627002, 627003"));
        stations.add(new PoliceStation("Pettai Police Station", "627004, 627010"));
        stations.add(new PoliceStation("Melapalayam Police Station", "627005, 627008, 627009"));
        stations.add(new PoliceStation("Tirunelveli Town Police Station", "627006, 627359"));
        stations.add(new PoliceStation("Perumalpuram Police Station", "627007"));
        stations.add(new PoliceStation("Medical College Police Station", "627011"));
        stations.add(new PoliceStation("Cyber Crime Police Station", "627011"));
        stations.add(new PoliceStation("Traffic Police Station", "627006"));
        // Tirunelveli District Police Stations
        stations.add(new PoliceStation("Manur Police Station", "627201, 627353"));
        stations.add(new PoliceStation("Seevalaperi Police Station", "627351"));
        stations.add(new PoliceStation("Gangaikondan Police Station", "627352"));
        stations.add(new PoliceStation("Moolakaraipatti Police Station", "627354"));
        stations.add(new PoliceStation("Thalaiyuthu Police Station", "627356"));
        stations.add(new PoliceStation("Moontradaippu Police Station", "627357"));
        stations.add(new PoliceStation("Thatchanallur Police Station", "627358"));
        stations.add(new PoliceStation("Ambasamudram Police Station", "627401, 627412, 627413, 627415, 627421, 627451, 627452, 627453"));
        stations.add(new PoliceStation("Nanguneri Police Station", "627101, 627103, 627105, 627108, 627109, 627110, 627116"));
        stations.add(new PoliceStation("Radhapuram Police Station", "627104, 627106, 627111, 627118, 627120, 627658"));
        stations.add(new PoliceStation("Thisayanvilai Police Station", "627657"));
        stations.add(new PoliceStation("Sivagiri Police Station", "627757"));
        policeStationRepository.saveAll(stations);
    }

    private void seedHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        // 627001 - Tirunelveli Junction
        hospitals.add(new Hospital("Tirunelveli Medical College Hospital", "627001, 627011"));
        hospitals.add(new Hospital("Getwell Hospital", "627001"));
        // 627002 - Palayamkottai
        hospitals.add(new Hospital("Government Hospital Palayamkottai", "627002"));
        hospitals.add(new Hospital("CSI Jayaraj Annapackiam Mission Hospital", "627002"));
        hospitals.add(new Hospital("Muthamil Hospital", "627002"));
        hospitals.add(new Hospital("Annai Velankanni Nursing Home", "627002"));
        hospitals.add(new Hospital("Krishna Hospital", "627002"));
        hospitals.add(new Hospital("SMS Hospital", "627002"));
        // 627004 - Pettai
        hospitals.add(new Hospital("Government Hospital Pettai", "627004"));
        hospitals.add(new Hospital("Sangeetha Hospital", "627004"));
        // 627005 - Melapalayam
        hospitals.add(new Hospital("Melapalayam Government Hospital", "627005"));
        hospitals.add(new Hospital("Peace Health Centre", "627005"));
        // 627006 - Tirunelveli Town
        hospitals.add(new Hospital("Kanthimadhi Hospital", "627006"));
        hospitals.add(new Hospital("Jaya Ratna Hospital", "627006"));
        // 627007 - Perumalpuram
        hospitals.add(new Hospital("Perumalpuram Government Hospital", "627007"));
        // 627008, 627009 - Kokkirakulam
        hospitals.add(new Hospital("Kokkirakulam Government Hospital", "627008"));
        // 627010 - Kallur
        hospitals.add(new Hospital("Kallur Government Hospital", "627010"));
        // 627201 - Manur
        hospitals.add(new Hospital("Manur Government Hospital", "627201"));
        hospitals.add(new Hospital("Manur Primary Health Centre", "627201"));
        // Other district hospitals
        hospitals.add(new Hospital("Seevalaperi Government Hospital", "627351"));
        hospitals.add(new Hospital("Gangaikondan Government Hospital", "627352"));
        hospitals.add(new Hospital("Nallur Government Hospital", "627353"));
        hospitals.add(new Hospital("Moolakaraipatti Government Hospital", "627354"));
        hospitals.add(new Hospital("Thalaiyuthu Government Hospital", "627356"));
        hospitals.add(new Hospital("Abishekapatti Government Hospital", "627357"));
        hospitals.add(new Hospital("Government Hospital Ambasamudram", "627401"));
        hospitals.add(new Hospital("Cheranmahadevi Government Hospital", "627414"));
        hospitals.add(new Hospital("Government Hospital Nanguneri", "627108"));
        hospitals.add(new Hospital("Government Hospital Radhapuram", "627111"));
        hospitals.add(new Hospital("Thisayanvilai Government Hospital", "627657"));
        hospitals.add(new Hospital("Sivagiri Government Hospital", "627757"));
        hospitalRepository.saveAll(hospitals);
    }

    // ============================================================
    // POLICE STATION CRUD
    // ============================================================

    public List<PoliceStation> getAllPoliceStations() {
        return policeStationRepository.findAll();
    }

    public PoliceStation getPoliceStationById(Long id) {
        return policeStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Police station not found with id: " + id));
    }

    @Transactional
    public PoliceStation addPoliceStation(String name, String pincodes) {
        if (policeStationRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Police station already exists: " + name);
        }
        return policeStationRepository.save(new PoliceStation(name, pincodes));
    }

    @Transactional
    public PoliceStation updatePoliceStation(Long id, String name, String pincodes) {
        PoliceStation station = getPoliceStationById(id);
        station.setName(name);
        station.setPincodes(pincodes);
        return policeStationRepository.save(station);
    }

    @Transactional
    public void deletePoliceStation(Long id) {
        if (!policeStationRepository.existsById(id)) {
            throw new RuntimeException("Police station not found with id: " + id);
        }
        policeStationRepository.deleteById(id);
    }

    public List<PoliceStation> searchPoliceByPincode(String pincode) {
        return policeStationRepository.findByPincodesContaining(pincode);
    }

    // ============================================================
    // HOSPITAL CRUD
    // ============================================================

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + id));
    }

    @Transactional
    public Hospital addHospital(String name, String pincodes) {
        if (hospitalRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Hospital already exists: " + name);
        }
        return hospitalRepository.save(new Hospital(name, pincodes));
    }

    @Transactional
    public Hospital updateHospital(Long id, String name, String pincodes) {
        Hospital hospital = getHospitalById(id);
        hospital.setName(name);
        hospital.setPincodes(pincodes);
        return hospitalRepository.save(hospital);
    }

    @Transactional
    public void deleteHospital(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    public List<Hospital> searchHospitalByPincode(String pincode) {
        return hospitalRepository.findByPincodesContaining(pincode);
    }
}
