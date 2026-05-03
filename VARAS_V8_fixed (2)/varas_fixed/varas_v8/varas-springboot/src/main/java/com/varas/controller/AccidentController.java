package com.varas.controller;

import com.varas.exception.InvalidCredentialsException;
import com.varas.exception.InvalidPincodeException;
import com.varas.model.Accident;
import com.varas.model.Hospital;
import com.varas.model.PoliceStation;
import com.varas.service.AccidentService;
import com.varas.service.PincodeDataService;
import com.varas.service.PoliceHospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    @Autowired
    private PincodeDataService pincodeDataService;

    @Autowired
    private PoliceHospitalService policeHospitalService;

    // ============================================================
    // PINCODE ENDPOINTS
    // ============================================================

    @GetMapping("/pincode/validate/{pincode}")
    public ResponseEntity<Map<String, Object>> validatePincode(@PathVariable String pincode) {
        boolean valid = pincodeDataService.isValidPincode(pincode);
        Map<String, Object> resp = new HashMap<>();
        resp.put("valid", valid);
        resp.put("pincode", pincode);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/pincode/{pincode}/villages")
    public ResponseEntity<Map<String, Object>> getVillages(@PathVariable String pincode) {
        if (!pincodeDataService.isValidPincode(pincode)) {
            throw new InvalidPincodeException(pincode);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("pincode", pincode);
        resp.put("villages", pincodeDataService.getVillagesByPincode(pincode));
        resp.put("policeStations", pincodeDataService.getNearbyPoliceStations(pincode));
        resp.put("hospitals", pincodeDataService.getNearbyHospitals(pincode));
        return ResponseEntity.ok(resp);
    }

    // ============================================================
    // REPORT ACCIDENT
    // ============================================================

    @PostMapping("/accidents/report")
    public ResponseEntity<Map<String, Object>> reportAccident(@RequestBody Map<String, String> request) {
        String reporterName = request.get("reporterName");
        String vehicleNumber = request.get("vehicleNumber");
        String village = request.get("village");
        String pincode = request.get("pincode");
        String severity = request.get("severity");

        if (vehicleNumber == null || !vehicleNumber.toUpperCase().matches("TN72[A-Z0-9]+")) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid vehicle number! Must start with TN72"
            ));
        }

        if (severity == null || (!severity.equalsIgnoreCase("low") &&
                !severity.equalsIgnoreCase("medium") &&
                !severity.equalsIgnoreCase("high"))) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid severity! Must be low, medium, or high"
            ));
        }

        List<String> policeStations = pincodeDataService.getNearbyPoliceStations(pincode);
        List<String> hospitals = pincodeDataService.getNearbyHospitals(pincode);

        Accident accident = accidentService.reportAccident(
                reporterName, vehicleNumber.toUpperCase(), village, pincode,
                severity.toLowerCase(), policeStations, hospitals);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Accident reported successfully!");
        resp.put("caseId", accident.getCaseId());
        resp.put("notifiedPolice", policeStations);
        resp.put("notifiedHospitals", hospitals);
        return ResponseEntity.ok(resp);
    }

    // ============================================================
    // POLICE ENDPOINTS
    // ============================================================

    // Per-station credentials map (username -> full station name)
    private static final Map<String, String> POLICE_CREDENTIALS = Map.of(
        "tvljunction",  "Tirunelveli Junction Police Station",
        "palayam",      "Palayamkottai Police Station",
        "pettai",       "Pettai Police Station",
        "melapalayam",  "Melapalayam Police Station"
    );

    @PostMapping("/police/login")
    public ResponseEntity<Map<String, Object>> policeLogin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // General police dashboard login: username=police, password=123
        if ("police".equals(username) && "123".equals(password)) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("role", "general");
            resp.put("message", "General police login successful");
            return ResponseEntity.ok(resp);
        }

        // Per-station login: station username + password 100
        if ("100".equals(password) && POLICE_CREDENTIALS.containsKey(username)) {
            String stationName = POLICE_CREDENTIALS.get(username);
            List<Map<String, String>> cases = accidentService.getCasesForPoliceStation(stationName);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("role", "station");
            resp.put("stationName", stationName);
            resp.put("username", username);
            resp.put("cases", cases);
            return ResponseEntity.ok(resp);
        }
        throw new InvalidCredentialsException("Police");
    }

    @PostMapping("/police/accept/{caseId}")
    public ResponseEntity<Map<String, Object>> acceptPoliceCase(@PathVariable String caseId) {
        boolean result = accidentService.acceptPoliceCaseSimple(caseId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", result);
        resp.put("message", result ? "Police Dispatched! Other police stations notified." : "Failed to accept case.");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/police/cases")
    public ResponseEntity<List<Map<String, String>>> getAllPoliceCases() {
        return ResponseEntity.ok(accidentService.getAllPoliceCases());
    }

    // ============================================================
    // HOSPITAL ENDPOINTS
    // ============================================================

    // Per-hospital credentials map
    private static final Map<String, String> HOSPITAL_CREDENTIALS = Map.of(
        "tvlmch",       "Tirunelveli Medical College Hospital",
        "getwell",      "Getwell Hospital",
        "govtpalayam",  "Government Hospital Palayamkottai",
        "govtpettai",   "Government Hospital Pettai",
        "sangeetha",    "Sangeetha Hospital",
        "melapalayam",  "Melapalayam Government Hospital"
    );

    @PostMapping("/hospital/login")
    public ResponseEntity<Map<String, Object>> hospitalLogin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // General hospital dashboard login: username=hospital, password=123
        if ("hospital".equals(username) && "123".equals(password)) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("role", "general");
            resp.put("message", "General hospital login successful");
            return ResponseEntity.ok(resp);
        }

        // Per-hospital login: hospital username + password 108
        if ("108".equals(password) && HOSPITAL_CREDENTIALS.containsKey(username)) {
            String hospitalName = HOSPITAL_CREDENTIALS.get(username);
            List<Map<String, String>> cases = accidentService.getCasesForHospital(hospitalName);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("role", "hospital");
            resp.put("hospitalName", hospitalName);
            resp.put("username", username);
            resp.put("cases", cases);
            return ResponseEntity.ok(resp);
        }
        throw new InvalidCredentialsException("Hospital");
    }

    @PostMapping("/hospital/accept/{caseId}")
    public ResponseEntity<Map<String, Object>> acceptHospitalCase(@PathVariable String caseId) {
        boolean result = accidentService.acceptHospitalCaseSimple(caseId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", result);
        resp.put("message", result ? "Ambulance Dispatched! Other hospitals notified." : "Failed to accept case.");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/hospital/cases")
    public ResponseEntity<List<Map<String, String>>> getAllHospitalCases() {
        return ResponseEntity.ok(accidentService.getAllHospitalCases());
    }

    // ============================================================
    // ADMIN ENDPOINTS
    // ============================================================

    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        if (!"admin".equals(username) || !"123".equals(password)) {
            throw new InvalidCredentialsException("Admin");
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Welcome Admin!");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/accidents")
    public ResponseEntity<Map<String, Object>> getAllAccidents() {
        List<Accident> accidents = accidentService.getAccidents();
        Map<String, Object> resp = new HashMap<>();
        resp.put("accidents", accidents);
        resp.put("total", accidents.size());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        int total = accidentService.getTotalAccidents();
        int high = accidentService.getHighSeverityCount();
        int medium = accidentService.getMediumSeverityCount();
        int low = accidentService.getLowSeverityCount();

        Map<String, Object> resp = new HashMap<>();
        resp.put("totalAccidents", total);
        resp.put("highSeverity", high);
        resp.put("mediumSeverity", medium);
        resp.put("lowSeverity", low);
        resp.put("acceptedPolice", accidentService.getAcceptedPoliceCount());
        resp.put("acceptedHospital", accidentService.getAcceptedHospitalCount());

        if (total > 0) {
            resp.put("highPercent", Math.round(high * 100.0 / total * 10.0) / 10.0);
            resp.put("mediumPercent", Math.round(medium * 100.0 / total * 10.0) / 10.0);
            resp.put("lowPercent", Math.round(low * 100.0 / total * 10.0) / 10.0);
        }
        return ResponseEntity.ok(resp);
    }

    // FIX: Use @RequestParam instead of @PathVariable for date (avoids '/' routing issue)
    @GetMapping("/admin/search/date")
    public ResponseEntity<Map<String, Object>> searchByDate(@RequestParam String date) {
        List<Accident> accidents = accidentService.getAccidentsByDate(date);
        Map<String, Object> resp = new HashMap<>();
        resp.put("date", date);
        resp.put("accidents", accidents);
        resp.put("total", accidents.size());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/search/pincode/{pincode}")
    public ResponseEntity<Map<String, Object>> searchByPincode(@PathVariable String pincode) {
        List<Accident> accidents = accidentService.getAccidentsByPincode(pincode);
        Map<String, Object> resp = new HashMap<>();
        resp.put("pincode", pincode);
        resp.put("accidents", accidents);
        resp.put("total", accidents.size());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/search/case/{caseId}")
    public ResponseEntity<Map<String, Object>> searchByCaseId(@PathVariable String caseId) {
        Accident accident = accidentService.getAccidentByCaseId(caseId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("accident", accident);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/search/severity/{severity}")
    public ResponseEntity<Map<String, Object>> searchBySeverity(@PathVariable String severity) {
        List<Accident> accidents = accidentService.getAccidentsBySeverity(severity);
        Map<String, Object> resp = new HashMap<>();
        resp.put("severity", severity.toUpperCase());
        resp.put("accidents", accidents);
        resp.put("total", accidents.size());
        return ResponseEntity.ok(resp);
    }

    // ============================================================
    // ADMIN - POLICE STATION CRUD
    // ============================================================

    @GetMapping("/admin/police-stations")
    public ResponseEntity<List<PoliceStation>> getAllPoliceStations() {
        return ResponseEntity.ok(policeHospitalService.getAllPoliceStations());
    }

    @PostMapping("/admin/police-stations")
    public ResponseEntity<Map<String, Object>> addPoliceStation(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String pincodes = body.get("pincodes");
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Station name is required"));
        }
        PoliceStation station = policeHospitalService.addPoliceStation(name.trim(), pincodes != null ? pincodes.trim() : "");
        return ResponseEntity.ok(Map.of("success", true, "message", "Police station added successfully", "station", station));
    }

    @PutMapping("/admin/police-stations/{id}")
    public ResponseEntity<Map<String, Object>> updatePoliceStation(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        String pincodes = body.get("pincodes");
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Station name is required"));
        }
        PoliceStation station = policeHospitalService.updatePoliceStation(id, name.trim(), pincodes != null ? pincodes.trim() : "");
        return ResponseEntity.ok(Map.of("success", true, "message", "Police station updated successfully", "station", station));
    }

    @DeleteMapping("/admin/police-stations/{id}")
    public ResponseEntity<Map<String, Object>> deletePoliceStation(@PathVariable Long id) {
        policeHospitalService.deletePoliceStation(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Police station deleted successfully"));
    }

    @GetMapping("/admin/police-stations/search")
    public ResponseEntity<List<PoliceStation>> searchPoliceByPincode(@RequestParam String pincode) {
        return ResponseEntity.ok(policeHospitalService.searchPoliceByPincode(pincode));
    }

    // ============================================================
    // ADMIN - HOSPITAL CRUD
    // ============================================================

    @GetMapping("/admin/hospitals")
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        return ResponseEntity.ok(policeHospitalService.getAllHospitals());
    }

    @PostMapping("/admin/hospitals")
    public ResponseEntity<Map<String, Object>> addHospital(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String pincodes = body.get("pincodes");
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Hospital name is required"));
        }
        Hospital hospital = policeHospitalService.addHospital(name.trim(), pincodes != null ? pincodes.trim() : "");
        return ResponseEntity.ok(Map.of("success", true, "message", "Hospital added successfully", "hospital", hospital));
    }

    @PutMapping("/admin/hospitals/{id}")
    public ResponseEntity<Map<String, Object>> updateHospital(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        String pincodes = body.get("pincodes");
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Hospital name is required"));
        }
        Hospital hospital = policeHospitalService.updateHospital(id, name.trim(), pincodes != null ? pincodes.trim() : "");
        return ResponseEntity.ok(Map.of("success", true, "message", "Hospital updated successfully", "hospital", hospital));
    }

    @DeleteMapping("/admin/hospitals/{id}")
    public ResponseEntity<Map<String, Object>> deleteHospital(@PathVariable Long id) {
        policeHospitalService.deleteHospital(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Hospital deleted successfully"));
    }

    @GetMapping("/admin/hospitals/search")
    public ResponseEntity<List<Hospital>> searchHospitalByPincode(@RequestParam String pincode) {
        return ResponseEntity.ok(policeHospitalService.searchHospitalByPincode(pincode));
    }

    // ============================================================
    // PINCODE REPORT — 5 KEY TVL PINCODES
    // ============================================================

    @GetMapping("/admin/pincode-report")
    public ResponseEntity<List<Map<String, Object>>> getPincodeReport() {
        List<String> targetPincodes = Arrays.asList("627001", "627002", "627004", "627005", "627006");
        List<Map<String, Object>> report = new ArrayList<>();

        Map<String, String> areaNames = new HashMap<>();
        areaNames.put("627001", "Tirunelveli Junction");
        areaNames.put("627002", "Palayamkottai");
        areaNames.put("627004", "Pettai");
        areaNames.put("627005", "Melapalayam");
        areaNames.put("627006", "Tirunelveli Town");

        for (String pincode : targetPincodes) {
            List<Accident> accidents = accidentService.getAccidentsByPincode(pincode);
            long high   = accidents.stream().filter(a -> "high".equalsIgnoreCase(a.getSeverity())).count();
            long medium = accidents.stream().filter(a -> "medium".equalsIgnoreCase(a.getSeverity())).count();
            long low    = accidents.stream().filter(a -> "low".equalsIgnoreCase(a.getSeverity())).count();
            long pending = accidents.stream()
                .filter(a -> a.getAcceptedPolice() == null || a.getAcceptedPolice().isBlank()
                          || a.getAcceptedHospital() == null || a.getAcceptedHospital().isBlank())
                .count();
            long accepted = accidents.stream()
                .filter(a -> a.getAcceptedPolice() != null && !a.getAcceptedPolice().isBlank()
                          && a.getAcceptedHospital() != null && !a.getAcceptedHospital().isBlank())
                .count();

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("pincode", pincode);
            entry.put("area", areaNames.getOrDefault(pincode, pincode));
            entry.put("total", accidents.size());
            entry.put("high", high);
            entry.put("medium", medium);
            entry.put("low", low);
            entry.put("pending", pending);
            entry.put("accepted", accepted);
            entry.put("police", pincodeDataService.getNearbyPoliceStations(pincode));
            entry.put("hospitals", pincodeDataService.getNearbyHospitals(pincode));
            report.add(entry);
        }
        return ResponseEntity.ok(report);
    }

    // ============================================================
    // HOSPITAL HISTORY — accepted cases by this hospital
    // ============================================================

    @PostMapping("/hospital/history")
    public ResponseEntity<Map<String, Object>> getHospitalHistory(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String hospitalName = request.get("hospitalName");
        if (!"hospital".equals(username) || !"123".equals(password)) {
            throw new InvalidCredentialsException("Hospital");
        }
        List<Map<String, String>> history = accidentService.getAcceptedCasesForHospital(hospitalName);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("history", history);
        resp.put("hospitalName", hospitalName);
        return ResponseEntity.ok(resp);
    }

    // ============================================================
    // POLICE HISTORY — accepted cases by this station
    // ============================================================

    @PostMapping("/police/history")
    public ResponseEntity<Map<String, Object>> getPoliceHistory(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String stationName = request.get("stationName");
        if (!"police".equals(username) || !"123".equals(password)) {
            throw new InvalidCredentialsException("Police");
        }
        List<Map<String, String>> history = accidentService.getAcceptedCasesForPoliceStation(stationName);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("history", history);
        resp.put("stationName", stationName);
        return ResponseEntity.ok(resp);
    }

    // ============================================================
    // HOSPITAL - Named login flow (select which hospital you are)
    // ============================================================

    @GetMapping("/hospital/list")
    public ResponseEntity<List<String>> getHospitalList() {
        // Only hospitals notified in actual accident cases
        return ResponseEntity.ok(accidentService.getNotifiedHospitals());
    }

    @PostMapping("/hospital/my-cases")
    public ResponseEntity<Map<String, Object>> getHospitalCases(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String hospitalName = request.get("hospitalName");
        if (!"hospital".equals(username) || !"123".equals(password)) {
            throw new InvalidCredentialsException("Hospital");
        }
        List<Map<String, String>> cases = accidentService.getCasesForHospital(hospitalName);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("cases", cases);
        resp.put("hospitalName", hospitalName);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/hospital/accept-named/{caseId}")
    public ResponseEntity<Map<String, Object>> acceptHospitalCaseNamed(
            @PathVariable String caseId, @RequestBody Map<String, String> request) {
        String hospitalName = request.get("hospitalName");
        try {
            boolean result = accidentService.acceptHospitalCase(caseId, hospitalName);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", result);
            resp.put("message", "Ambulance Dispatched by " + hospitalName + "!");
            return ResponseEntity.ok(resp);
        } catch (com.varas.exception.CaseAlreadyAcceptedException e) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ============================================================
    // POLICE - Named login flow (select which station you are)
    // ============================================================

    @GetMapping("/police/list")
    public ResponseEntity<List<String>> getPoliceList() {
        // Only police stations notified in actual accident cases
        return ResponseEntity.ok(accidentService.getNotifiedPoliceStations());
    }

    @PostMapping("/police/my-cases")
    public ResponseEntity<Map<String, Object>> getPoliceCases(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String stationName = request.get("stationName");
        // Accept general login token (username=police, password=123)
        if (!"police".equals(username) || !"123".equals(password)) {
            throw new InvalidCredentialsException("Police");
        }
        List<Map<String, String>> cases = accidentService.getCasesForPoliceStation(stationName);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("cases", cases);
        resp.put("stationName", stationName);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/police/accept-named/{caseId}")
    public ResponseEntity<Map<String, Object>> acceptPoliceCaseNamed(
            @PathVariable String caseId, @RequestBody Map<String, String> request) {
        String stationName = request.get("stationName");
        try {
            boolean result = accidentService.acceptPoliceCase(caseId, stationName);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", result);
            resp.put("message", "Police Dispatched by " + stationName + "!");
            return ResponseEntity.ok(resp);
        } catch (com.varas.exception.CaseAlreadyAcceptedException e) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
