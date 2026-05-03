package com.varas.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PincodeDataService {

    private static final Map<String, List<String>> villagesByPincode = new HashMap<>();
    private static final Map<String, String> villageLocationMap = new HashMap<>();
    private static final Map<String, List<String>> pincodePoliceMap = new HashMap<>();
    private static final Map<String, List<String>> pincodeHospitalMap = new HashMap<>();

    static {
        addPincodeData("627001",
            Arrays.asList("Tirunelveli Junction", "Junction Area", "KTC Nagar", "KTC Colony",
                "High Ground", "Anna Nagar", "Maharaja Nagar", "Indira Nagar", "Bharathi Nagar"),
            "Tirunelveli Junction Area",
            Arrays.asList("Tirunelveli Junction Police Station"),
            Arrays.asList("Tirunelveli Medical College Hospital", "Getwell Hospital"));

        addPincodeData("627002",
            Arrays.asList("Palayamkottai", "Palayapettai", "Samathanapuram", "Shanthi Nagar",
                "Reddiyarpatti", "New Colony", "NGO Colony"),
            "Palayamkottai Area",
            Arrays.asList("Palayamkottai Police Station"),
            Arrays.asList("Government Hospital Palayamkottai", "CSI Jayaraj Annapackiam Mission Hospital",
                "Muthamil Hospital", "Krishna Hospital", "SMS Hospital"));

        addPincodeData("627003",
            Arrays.asList("Vannarpettai"),
            "Vannarpettai Area",
            Arrays.asList("Palayamkottai Police Station"),
            Arrays.asList("Dr. Velayudhan Pillai Hospital"));

        addPincodeData("627004",
            Arrays.asList("Pettai", "Kovilpatti Road Area"),
            "Pettai Area",
            Arrays.asList("Pettai Police Station"),
            Arrays.asList("Government Hospital Pettai", "Sangeetha Hospital"));

        addPincodeData("627005",
            Arrays.asList("Melapalayam", "Melaputhaneri", "Vagaikulam", "Thenpathu"),
            "Melapalayam Area",
            Arrays.asList("Melapalayam Police Station"),
            Arrays.asList("Melapalayam Government Hospital", "Peace Health Centre"));

        addPincodeData("627006",
            Arrays.asList("Tirunelveli Town", "Sankarankovil Road Area"),
            "Tirunelveli Town Area",
            Arrays.asList("Tirunelveli Town Police Station", "Traffic Police Station"),
            Arrays.asList("Kanthimadhi Hospital", "Jaya Ratna Hospital"));

        addPincodeData("627007",
            Arrays.asList("Perumalpuram", "Perumal Nagar"),
            "Perumalpuram Area",
            Arrays.asList("Perumalpuram Police Station"),
            Arrays.asList("Perumalpuram Government Hospital"));

        addPincodeData("627008",
            Arrays.asList("Kokkirakulam"),
            "Kokkirakulam Area",
            Arrays.asList("Melapalayam Police Station"),
            Arrays.asList("Kokkirakulam Government Hospital"));

        addPincodeData("627009",
            Arrays.asList("Kokirakulam"),
            "Kokirakulam Area",
            Arrays.asList("Melapalayam Police Station"),
            Arrays.asList("Kokirakulam Government Hospital"));

        addPincodeData("627010",
            Arrays.asList("Kallur", "Melakallur", "Keelakallur", "Kurukkuthurai"),
            "Kallur Area",
            Arrays.asList("Pettai Police Station"),
            Arrays.asList("Kallur Government Hospital"));

        addPincodeData("627011",
            Arrays.asList("Medical College Area", "High Ground", "KTC Nagar", "KTC Colony",
                "Maharaja Nagar", "Indira Nagar", "Bharathi Nagar", "Anna Nagar"),
            "Medical College Area",
            Arrays.asList("Medical College Police Station", "Cyber Crime Police Station"),
            Arrays.asList("Tirunelveli Medical College Hospital", "Getwell Hospital"));

        addPincodeData("627201",
            Arrays.asList("Manur", "Alagiapandiapuram", "Ettankulam", "Kalyanipuram", "Melapatti",
                "Nallur", "Othapatti", "Pullandalam", "Ramasamiyapuram", "Serndamangalam",
                "Umaiyalpuram", "Vadakkupatti", "Venkatasamudram", "Thiruppani Karisal", "Udayaneri",
                "Karisalkulam", "Kottaiyur", "Sundareswarapuram", "Vellalankulam", "Mela Thidiyur",
                "Keela Thidiyur", "Kurichi", "Thulukkapatti", "Alagapuri", "Melapavoor",
                "Keelapavoor", "Karisal Colony", "Muthusamudram", "Sivagurunathapuram", "Pandarakulam Colony",
                "Nambinagar", "Kalikudi", "Melakadu", "Keelakadu", "Vannikonendal",
                "Perinbathur", "Ayyanarkulam", "Velankulam", "Sivanthipatti", "Rettiyarpatti",
                "Kondanagaram", "Karisalpudur", "Moolakarai", "Peththanthurai", "Kuppakurichi",
                "Alangaraperi", "Muthur", "Kurumbur"),
            "Manur Area",
            Arrays.asList("Manur Police Station"),
            Arrays.asList("Manur Government Hospital", "Manur Primary Health Centre"));

        addPincodeData("627351",
            Arrays.asList("Seevalaperi", "Naranammalpuram", "Maruthur", "Vazhuthur", "Mela Veeranam",
                "Pappankulam", "Karisalpatti", "Pandarakulam"),
            "Seevalaperi Area",
            Arrays.asList("Seevalaperi Police Station"),
            Arrays.asList("Seevalaperi Government Hospital"));

        addPincodeData("627352",
            Arrays.asList("Gangaikondan", "Kandiaperi"),
            "Gangaikondan Area",
            Arrays.asList("Gangaikondan Police Station"),
            Arrays.asList("Gangaikondan Government Hospital"));

        addPincodeData("627353",
            Arrays.asList("Nallur", "Melapatti", "Othapatti", "Pullandalam", "Ramasamiyapuram",
                "Serndamangalam", "Umaiyalpuram", "Vadakkupatti", "Venkatasamudram", "Thiruppani Karisal",
                "Udayaneri", "Karisalkulam", "Kottaiyur", "Sundareswarapuram", "Vellalankulam",
                "Mela Thidiyur", "Keela Thidiyur", "Kurichi", "Thulukkapatti", "Alagapuri",
                "Melapavoor", "Keelapavoor", "Karisal Colony", "Muthusamudram", "Sivagurunathapuram",
                "Pandarakulam Colony", "Nambinagar", "Kalikudi", "Melakadu", "Keelakadu",
                "Vannikonendal", "Perinbathur", "Ayyanarkulam", "Velankulam", "Sivanthipatti",
                "Rettiyarpatti", "Kondanagaram", "Karisalpudur", "Moolakarai"),
            "Nallur Area",
            Arrays.asList("Manur Police Station"),
            Arrays.asList("Nallur Government Hospital"));

        addPincodeData("627354",
            Arrays.asList("Moolakaraipatti", "Ramayanpatti", "Deivanayagaperi", "Kadamboduvalvu", "Koothankulam",
                "Sanganeri", "Thidiyur", "Thenmalai", "Vadivelpatti", "Moolaikaraipatti",
                "Sathankulam", "Paruthipadu", "Sivaramapuram", "Muthukrishnapuram", "Thulukkapatti",
                "Velankulam", "Karungulam", "Kallikulam"),
            "Moolakaraipatti Area",
            Arrays.asList("Moolakaraipatti Police Station"),
            Arrays.asList("Moolakaraipatti Government Hospital"));

        addPincodeData("627356",
            Arrays.asList("Thalaiyuthu", "Thalaiyuthu Colony"),
            "Thalaiyuthu Area",
            Arrays.asList("Thalaiyuthu Police Station"),
            Arrays.asList("Thalaiyuthu Government Hospital"));

        addPincodeData("627357",
            Arrays.asList("Abishekapatti", "Ukkirankottai", "Sivalapperi", "Melur", "Kunnathur",
                "Sivandipatti", "Karungulam", "Pudur", "Thenpathu Colony"),
            "Abishekapatti Area",
            Arrays.asList("Moontradaippu Police Station"),
            Arrays.asList("Abishekapatti Government Hospital"));

        addPincodeData("627358",
            Arrays.asList("Thatchanallur"),
            "Thatchanallur Area",
            Arrays.asList("Thatchanallur Police Station"),
            Arrays.asList("Thatchanallur Government Hospital"));

        addPincodeData("627359",
            Arrays.asList("Rajavallipuram"),
            "Rajavallipuram Area",
            Arrays.asList("Tirunelveli Town Police Station"),
            Arrays.asList("Rajavallipuram Government Hospital"));

        addPincodeData("627401",
            Arrays.asList("Ambasamudram", "Idaikal", "Pottalpatti", "Thamaraikulam", "Vasanthapuram",
                "Ayyanarkulam", "Kottaiyur", "Alwarkurichi", "Sivasailam", "Harikesavanallur",
                "Thiruvaliswaram", "Puliyur", "Nadukkurichi"),
            "Ambasamudram Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Government Hospital Ambasamudram"));

        addPincodeData("627412",
            Arrays.asList("Papanasam", "Alwarkurichi", "Sivasailam", "Thiruvaliswaram", "Puliyur"),
            "Papanasam Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Papanasam Government Hospital"));

        addPincodeData("627413",
            Arrays.asList("Brahmadesam", "Mannarkoil"),
            "Brahmadesam Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Brahmadesam Government Hospital"));

        addPincodeData("627414",
            Arrays.asList("Cheranmahadevi", "Kodarangulam", "Kooniyur", "Karisalpatti", "Melambur",
                "Kila Ambur", "Vellanguli", "Pudukudi", "Serndamangalam", "Kadayam Colony",
                "Sundarapandiapuram"),
            "Cheranmahadevi Area",
            Arrays.asList("Cheranmahadevi Police Station"),
            Arrays.asList("Cheranmahadevi Government Hospital"));

        addPincodeData("627415",
            Arrays.asList("Kadayam", "Kadayam Colony", "Sundarapandiapuram"),
            "Kadayam Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Kadayam Government Hospital"));

        addPincodeData("627416",
            Arrays.asList("Kallidaikurichi", "Kallidaikurichi Rural"),
            "Kallidaikurichi Area",
            Arrays.asList("Kallidaikurichi Police Station"),
            Arrays.asList("Kallidaikurichi Government Hospital"));

        addPincodeData("627421",
            Arrays.asList("Thiruppudaimaruthur"),
            "Thiruppudaimaruthur Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Thiruppudaimaruthur Government Hospital"));

        addPincodeData("627425",
            Arrays.asList("Vikramasingapuram"),
            "Vikramasingapuram Area",
            Arrays.asList("Vikramasingapuram Police Station"),
            Arrays.asList("Vikramasingapuram Government Hospital"));

        addPincodeData("627426",
            Arrays.asList("Veeravanallur", "Harikesavanallur"),
            "Veeravanallur Area",
            Arrays.asList("Veeravanallur Police Station"),
            Arrays.asList("Veeravanallur Government Hospital"));

        addPincodeData("627451",
            Arrays.asList("Gopalasamudram", "Govindapperi", "Sengulam", "Prancheri", "Aladiyur"),
            "Gopalasamudram Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Gopalasamudram Government Hospital"));

        addPincodeData("627452",
            Arrays.asList("Melacheval"),
            "Melacheval Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Melacheval Government Hospital"));

        addPincodeData("627453",
            Arrays.asList("Pathamadai"),
            "Pathamadai Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Pathamadai Government Hospital"));

        addPincodeData("627101",
            Arrays.asList("Eathamozhi", "Thalapathi Samudram", "Dalapathisamudram"),
            "Eathamozhi Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Eathamozhi Government Hospital"));

        addPincodeData("627103",
            Arrays.asList("Ervadi", "Kiliyur", "Ariyakulam"),
            "Ervadi Area",
            Arrays.asList("Ervadi Police Station"),
            Arrays.asList("Ervadi Government Hospital"));

        addPincodeData("627104",
            Arrays.asList("Ithamozhi", "Ittamozhi", "Idinthakarai", "Vijayapathi", "Kootapuli", "Kuthankuzhi"),
            "Ithamozhi Area",
            Arrays.asList("Ithamozhi Police Station"),
            Arrays.asList("Ithamozhi Government Hospital"));

        addPincodeData("627105",
            Arrays.asList("Kaval Kinaru"),
            "Kaval Kinaru Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Kaval Kinaru Government Hospital"));

        addPincodeData("627106",
            Arrays.asList("Kudankulam", "Koodankulam Colony"),
            "Kudankulam Area",
            Arrays.asList("Radhapuram Police Station"),
            Arrays.asList("Kudankulam Government Hospital"));

        addPincodeData("627108",
            Arrays.asList("Nanguneri", "Perungudi", "Navaladi"),
            "Nanguneri Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Government Hospital Nanguneri"));

        addPincodeData("627109",
            Arrays.asList("Panagudi", "Panagudi Coastal"),
            "Panagudi Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Panagudi Government Hospital"));

        addPincodeData("627110",
            Arrays.asList("Parappadi"),
            "Parappadi Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Parappadi Government Hospital"));

        addPincodeData("627111",
            Arrays.asList("Radhapuram", "Radhapuram Colony"),
            "Radhapuram Area",
            Arrays.asList("Radhapuram Police Station"),
            Arrays.asList("Government Hospital Radhapuram"));

        addPincodeData("627115",
            Arrays.asList("Thirukurungudi"),
            "Thirukurungudi Area",
            Arrays.asList("Thirukurungudi Police Station"),
            Arrays.asList("Thirukurungudi Government Hospital"));

        addPincodeData("627116",
            Arrays.asList("Vadakkankulam", "Danakkarkulam", "Veppilankulam", "Kallikulam"),
            "Vadakkankulam Area",
            Arrays.asList("Nanguneri Police Station"),
            Arrays.asList("Vadakkankulam Government Hospital"));

        addPincodeData("627117",
            Arrays.asList("Valliyoor", "South Valliyoor"),
            "Valliyoor Area",
            Arrays.asList("Valliyoor Police Station"),
            Arrays.asList("Valliyoor Government Hospital"));

        addPincodeData("627118",
            Arrays.asList("Vijayanarayanam"),
            "Vijayanarayanam Area",
            Arrays.asList("Radhapuram Police Station"),
            Arrays.asList("Vijayanarayanam Government Hospital"));

        addPincodeData("627120",
            Arrays.asList("Chettikulam"),
            "Chettikulam Area",
            Arrays.asList("Radhapuram Police Station"),
            Arrays.asList("Chettikulam Government Hospital"));

        addPincodeData("627501",
            Arrays.asList("Kalakkadu", "Kalakkad RF Area"),
            "Kalakkadu Area",
            Arrays.asList("Kalakkadu Police Station"),
            Arrays.asList("Kalakkadu Government Hospital"));

        addPincodeData("627601",
            Arrays.asList("Mukkudal"),
            "Mukkudal Area",
            Arrays.asList("Ambasamudram Police Station"),
            Arrays.asList("Mukkudal Government Hospital"));

        addPincodeData("627602",
            Arrays.asList("Pappakudi", "Pottalpatti", "Thamaraikulam", "Vasanthapuram", "Ayyanarkulam",
                "Kottaiyur", "Idaikal"),
            "Pappakudi Area",
            Arrays.asList("Pappakudi Police Station"),
            Arrays.asList("Pappakudi Government Hospital"));

        addPincodeData("627604",
            Arrays.asList("Suthamalli"),
            "Suthamalli Area",
            Arrays.asList("Suthamalli Police Station"),
            Arrays.asList("Suthamalli Government Hospital"));

        addPincodeData("627657",
            Arrays.asList("Thisayanvilai", "Navaladi", "Samugarengapuram", "Karaisuthuputhur", "Kumarapuram",
                "Mahadevan Kulam", "Kuttam", "Anaikulam", "Karaichithupudur", "Kurugapuram",
                "Mannarpuram", "Puthukarai", "Sirunelveli", "Samaria", "Perumpathu",
                "Tisayanvilai Rural", "Kottankulam", "Kanyakumari Border Hamlet"),
            "Thisayanvilai Area",
            Arrays.asList("Thisayanvilai Police Station"),
            Arrays.asList("Thisayanvilai Government Hospital"));

        addPincodeData("627658",
            Arrays.asList("Uvari", "Uvari Colony"),
            "Uvari Area",
            Arrays.asList("Radhapuram Police Station"),
            Arrays.asList("Uvari Government Hospital"));

        addPincodeData("627757",
            Arrays.asList("Sivagiri"),
            "Sivagiri Area",
            Arrays.asList("Sivagiri Police Station"),
            Arrays.asList("Sivagiri Government Hospital"));

        addPincodeData("627758",
            Arrays.asList("Devarkulam"),
            "Devarkulam Area",
            Arrays.asList("Devarkulam Police Station"),
            Arrays.asList("Devarkulam Government Hospital"));
    }

    private static void addPincodeData(String pincode, List<String> villages, String location,
                                        List<String> policeStations, List<String> hospitals) {
        villagesByPincode.put(pincode, villages);
        for (String v : villages) {
            villageLocationMap.put(v, location);
        }
        pincodePoliceMap.put(pincode, policeStations);
        pincodeHospitalMap.put(pincode, hospitals);
    }

    public List<String> getVillagesByPincode(String pincode) {
        return villagesByPincode.getOrDefault(pincode, new ArrayList<>());
    }

    public List<String> getNearbyPoliceStations(String pincode) {
        return pincodePoliceMap.getOrDefault(pincode, new ArrayList<>());
    }

    public List<String> getNearbyHospitals(String pincode) {
        return pincodeHospitalMap.getOrDefault(pincode, new ArrayList<>());
    }

    public String getLocationDetails(String pincode, String village) {
        return villageLocationMap.getOrDefault(village, village + ", Tirunelveli District");
    }

    public boolean isValidPincode(String pincode) {
        return villagesByPincode.containsKey(pincode);
    }

    public int getTotalPincodes() {
        return villagesByPincode.size();
    }

    public Set<String> getAllPincodes() {
        return villagesByPincode.keySet();
    }

    // All police station data (same as PoliceData.java)
    public List<Map<String, String>> getAllPoliceStations() {
        List<Map<String, String>> stations = new ArrayList<>();
        stations.add(stationEntry("1", "Tirunelveli Junction Police Station", "627001"));
        stations.add(stationEntry("2", "Palayamkottai Police Station", "627002, 627003"));
        stations.add(stationEntry("3", "Pettai Police Station", "627004, 627010"));
        stations.add(stationEntry("4", "Melapalayam Police Station", "627005, 627008, 627009"));
        stations.add(stationEntry("5", "Tirunelveli Town Police Station", "627006, 627359"));
        stations.add(stationEntry("6", "Perumalpuram Police Station", "627007"));
        stations.add(stationEntry("7", "Medical College Police Station", "627011"));
        stations.add(stationEntry("8", "Cyber Crime Police Station", "627011"));
        stations.add(stationEntry("9", "Traffic Police Station", "627006"));
        stations.add(stationEntry("10", "Manur Police Station", "627201, 627353"));
        stations.add(stationEntry("11", "Seevalaperi Police Station", "627351"));
        stations.add(stationEntry("12", "Gangaikondan Police Station", "627352"));
        stations.add(stationEntry("13", "Moolakaraipatti Police Station", "627354"));
        stations.add(stationEntry("14", "Thalaiyuthu Police Station", "627356"));
        stations.add(stationEntry("15", "Moontradaippu Police Station", "627357"));
        stations.add(stationEntry("16", "Thatchanallur Police Station", "627358"));
        stations.add(stationEntry("17", "Ambasamudram Police Station", "627401, 627412, 627413, 627415, 627421, 627451, 627452, 627453"));
        stations.add(stationEntry("18", "Nanguneri Police Station", "627101, 627103, 627105, 627108, 627109, 627110, 627116"));
        stations.add(stationEntry("19", "Radhapuram Police Station", "627104, 627106, 627111, 627118, 627120, 627658"));
        stations.add(stationEntry("20", "Thisayanvilai Police Station", "627657"));
        stations.add(stationEntry("21", "Sivagiri Police Station", "627757"));
        return stations;
    }

    // All hospital data (same as HospitalData.java)
    public List<Map<String, String>> getAllHospitals() {
        List<Map<String, String>> hospitals = new ArrayList<>();
        hospitals.add(hospitalEntry("1", "Tirunelveli Medical College Hospital", "627001, 627011"));
        hospitals.add(hospitalEntry("2", "Getwell Hospital", "627001"));
        hospitals.add(hospitalEntry("3", "Government Hospital Palayamkottai", "627002"));
        hospitals.add(hospitalEntry("4", "CSI Jayaraj Annapackiam Mission Hospital", "627002"));
        hospitals.add(hospitalEntry("5", "Muthamil Hospital", "627002"));
        hospitals.add(hospitalEntry("6", "Annai Velankanni Nursing Home", "627002"));
        hospitals.add(hospitalEntry("7", "Krishna Hospital", "627002"));
        hospitals.add(hospitalEntry("8", "SMS Hospital", "627002"));
        hospitals.add(hospitalEntry("9", "Government Hospital Pettai", "627004"));
        hospitals.add(hospitalEntry("10", "Sangeetha Hospital", "627004"));
        hospitals.add(hospitalEntry("11", "Melapalayam Government Hospital", "627005"));
        hospitals.add(hospitalEntry("12", "Peace Health Centre", "627005"));
        hospitals.add(hospitalEntry("13", "Kanthimadhi Hospital", "627006"));
        hospitals.add(hospitalEntry("14", "Jaya Ratna Hospital", "627006"));
        hospitals.add(hospitalEntry("15", "Perumalpuram Government Hospital", "627007"));
        hospitals.add(hospitalEntry("16", "Kokkirakulam Government Hospital", "627008"));
        hospitals.add(hospitalEntry("17", "Kallur Government Hospital", "627010"));
        hospitals.add(hospitalEntry("18", "Manur Government Hospital", "627201"));
        hospitals.add(hospitalEntry("19", "Manur Primary Health Centre", "627201"));
        hospitals.add(hospitalEntry("20", "Seevalaperi Government Hospital", "627351"));
        hospitals.add(hospitalEntry("21", "Gangaikondan Government Hospital", "627352"));
        hospitals.add(hospitalEntry("22", "Nallur Government Hospital", "627353"));
        hospitals.add(hospitalEntry("23", "Moolakaraipatti Government Hospital", "627354"));
        hospitals.add(hospitalEntry("24", "Thalaiyuthu Government Hospital", "627356"));
        hospitals.add(hospitalEntry("25", "Abishekapatti Government Hospital", "627357"));
        hospitals.add(hospitalEntry("26", "Government Hospital Ambasamudram", "627401"));
        hospitals.add(hospitalEntry("27", "Cheranmahadevi Government Hospital", "627414"));
        hospitals.add(hospitalEntry("28", "Government Hospital Nanguneri", "627108"));
        hospitals.add(hospitalEntry("29", "Government Hospital Radhapuram", "627111"));
        hospitals.add(hospitalEntry("30", "Thisayanvilai Government Hospital", "627657"));
        hospitals.add(hospitalEntry("31", "Sivagiri Government Hospital", "627757"));
        return hospitals;
    }

    private Map<String, String> stationEntry(String no, String name, String pincodes) {
        Map<String, String> m = new HashMap<>();
        m.put("no", no);
        m.put("name", name);
        m.put("pincodes", pincodes);
        return m;
    }

    private Map<String, String> hospitalEntry(String no, String name, String pincodes) {
        Map<String, String> m = new HashMap<>();
        m.put("no", no);
        m.put("name", name);
        m.put("pincodes", pincodes);
        return m;
    }
}
