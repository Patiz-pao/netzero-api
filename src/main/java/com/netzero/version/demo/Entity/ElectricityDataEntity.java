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
@Table(name = "nz_electricity_data")
public class ElectricityDataEntity {

    @Id
    @Column(name = "response_id")
    private String responseId;

    @Column(name = "electricity_required")
    private String electricityRequired;

    @Column(name = "electricity_produced")
    private String electricityProduced;

    @Column(name = "electricity_surplus")
    private String electricitySurplus;
}
