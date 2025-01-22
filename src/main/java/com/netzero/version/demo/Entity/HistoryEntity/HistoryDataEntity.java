package com.netzero.version.demo.Entity.HistoryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "nz_history_data")
public class HistoryDataEntity {
    @Id
    @Column(name = "history_id")
    private String historyId;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "solar_energy_intensity")
    private Double solarEnergyIntensity;

    @Column(name = "number_of_panels")
    private Integer numberOfPanels;

    @Column(name = "required_electricity")
    private Double requiredElectricity;

    @Column(name = "usable_electricity")
    private Double usableElectricity;

    @Column(name = "surplus_electricity_usable")
    private Double surplusElectricityUsable;

    @Column(name = "produced_electricity")
    private Double producedElectricity;

    @Column(name = "surplus_electricity")
    private Double surplusElectricity;

    @Column(name = "area_used")
    private Double areaUsed;

    @Column(name = "area_remaining")
    private Double areaRemaining;

    @Column(name = "result_rice")
    private Integer resultRice;

    @Column(name = "energy_tractor")
    private Double energyTractor;

    @Column(name = "energy_drone")
    private Double energyDrone;

    @Column(name = "energy_water_pumping")
    private Double energyWaterPumping;

    @Column(name = "startdate")
    private LocalDate startDate;

    @Column(name = "enddate")
    private LocalDate endDate;
}
