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

    //TODO:วางยาน้าาาตรงนี้ใน DB ไม่มี number_panel_count น้าๆ ตอนมัน Error:500 กุแบบไรวะ โค้ดก็ถูกหนิ หรือกุลืมที่มึงพูดนี่แหละ55555555

    @Column(name = "solar_panel_count")
    private Integer solarPanelCount;
}
