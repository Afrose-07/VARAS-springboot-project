package com.varas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pincodes", length = 500)
    private String pincodes;

    public Hospital() {}

    public Hospital(String name, String pincodes) {
        this.name = name;
        this.pincodes = pincodes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPincodes() { return pincodes; }
    public void setPincodes(String pincodes) { this.pincodes = pincodes; }
}
