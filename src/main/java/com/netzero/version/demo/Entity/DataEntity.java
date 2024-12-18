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
@Table(name = "nz_data")
public class DataEntity {
    @Id
    @Column(name = "response_id")
    private String responseId;

    @Column(name = "crop_type")
    private String cropType;

    @Column(name = "area_size")
    private String areaSize;

    @Column(name = "solar_panel_count")
    private Integer solarPanelCount;
}
