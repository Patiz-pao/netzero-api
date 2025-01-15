package com.netzero.version.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "nz_solar_intensity")
public class SolarIntEntity {
    @Id
    @Column(name = "intensity_id")
    private String intensityId;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "jan")
    private String jan;

    @Column(name = "feb")
    private String feb;

    @Column(name = "mar")
    private String mar;

    @Column(name = "apr")
    private String apr;

    @Column(name = "may")
    private String may;

    @Column(name = "jun")
    private String jun;

    @Column(name = "jul")
    private String jul;

    @Column(name = "aug")
    private String aug;

    @Column(name = "sep")
    private String sep;

    @Column(name = "oct")
    private String oct;

    @Column(name = "nov")
    private String nov;

    @Column(name = "dec")
    private String dec;
}
