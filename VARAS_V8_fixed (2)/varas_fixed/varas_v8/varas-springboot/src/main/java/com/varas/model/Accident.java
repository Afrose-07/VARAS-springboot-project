package com.varas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "accidents")
public class Accident {

    @Id
    @Column(name = "case_id", nullable = false, unique = true)
    private String caseId;

    @Column(name = "reporter_name", nullable = false)
    private String reporterName;

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "village", nullable = false)
    private String village;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Column(name = "severity", nullable = false)
    private String severity;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "report_date_time", nullable = false)
    private LocalDateTime reportDateTime;

    // Stored as comma-separated
    @Column(name = "notified_police_stations", length = 1000)
    private String notifiedPoliceStations;

    // Stored as comma-separated
    @Column(name = "notified_hospitals", length = 1000)
    private String notifiedHospitals;

    @Column(name = "accepted_police")
    private String acceptedPolice;

    @Column(name = "accepted_hospital")
    private String acceptedHospital;

    public Accident() {}

    public Accident(String caseId, String reporterName, String vehicleNumber,
                    String village, String pincode, String severity,
                    String notifiedPoliceStations, String notifiedHospitals) {
        this.caseId = caseId;
        this.reporterName = reporterName;
        this.vehicleNumber = vehicleNumber;
        this.village = village;
        this.pincode = pincode;
        this.severity = severity;
        this.status = "Reported - Awaiting Response";
        this.reportDateTime = LocalDateTime.now();
        this.notifiedPoliceStations = notifiedPoliceStations;
        this.notifiedHospitals = notifiedHospitals;
        this.acceptedPolice = null;
        this.acceptedHospital = null;
    }

    // ====== Getters ======
    public String getCaseId() { return caseId; }
    public String getReporterName() { return reporterName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getVillage() { return village; }
    public String getPincode() { return pincode; }
    public String getSeverity() { return severity; }
    public String getStatus() { return status; }
    public LocalDateTime getReportDateTime() { return reportDateTime; }
    public String getNotifiedPoliceStations() { return notifiedPoliceStations; }
    public String getNotifiedHospitals() { return notifiedHospitals; }
    public String getAcceptedPolice() { return acceptedPolice; }
    public String getAcceptedHospital() { return acceptedHospital; }

    // ====== Setters ======
    public void setCaseId(String caseId) { this.caseId = caseId; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setVillage(String village) { this.village = village; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setStatus(String status) { this.status = status; }
    public void setReportDateTime(LocalDateTime reportDateTime) { this.reportDateTime = reportDateTime; }
    public void setNotifiedPoliceStations(String notifiedPoliceStations) { this.notifiedPoliceStations = notifiedPoliceStations; }
    public void setNotifiedHospitals(String notifiedHospitals) { this.notifiedHospitals = notifiedHospitals; }
    public void setAcceptedPolice(String acceptedPolice) { this.acceptedPolice = acceptedPolice; }
    public void setAcceptedHospital(String acceptedHospital) { this.acceptedHospital = acceptedHospital; }

    // ====== Helper Methods (same as original) ======
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return reportDateTime.format(formatter);
    }

    public String getDateOnly() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return reportDateTime.format(formatter);
    }

    public boolean isPoliceAccepted() {
        return acceptedPolice != null && !acceptedPolice.isEmpty();
    }

    public boolean isHospitalAccepted() {
        return acceptedHospital != null && !acceptedHospital.isEmpty();
    }
}
