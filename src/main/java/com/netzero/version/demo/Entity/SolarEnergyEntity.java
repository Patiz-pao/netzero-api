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
@Table(name = "nz_solar_energy")
public class SolarEnergyEntity {

    @Id
    @Column(name = "response_id")
    private String responseId;

    @Column(name = "solar_intensity")
    private String solarIntensity;

    @Column(name = "solar_panel_count")
    private Integer solarPanelCount;
}
